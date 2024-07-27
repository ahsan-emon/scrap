package com.ahsan.scrap.controller.web;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ahsan.scrap.model.Customer;
import com.ahsan.scrap.model.Order;
import com.ahsan.scrap.model.OrderItem;
import com.ahsan.scrap.model.Product;
import com.ahsan.scrap.model.UserDtls;
import com.ahsan.scrap.model.Vehicle;
import com.ahsan.scrap.repository.CustomerRepository;
import com.ahsan.scrap.repository.OrderItemRepository;
import com.ahsan.scrap.repository.OrderRepository;
import com.ahsan.scrap.repository.ProductRepository;
import com.ahsan.scrap.repository.UserRepository;
import com.ahsan.scrap.repository.VehicleRepository;
import com.ahsan.scrap.service.CustomerService;
import com.ahsan.scrap.service.OrderService;
import com.ahsan.scrap.service.ProductService;
import com.ahsan.scrap.service.UserService;
import com.ahsan.scrap.service.VehicleService;
import com.ahsan.scrap.util.FileUploadService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
    private UserRepository userRepository;
	@Autowired
    private CustomerRepository customerRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private VehicleRepository vehicleRepository;
	@Autowired
	private OrderRepository orderRepository ;
	@Autowired
	private OrderItemRepository orderItemRepository ;
	@Autowired
    private CustomerService customerService;
	@Autowired
    private UserService userService;
	@Autowired
    private ProductService productService;
	@Autowired
    private VehicleService vehicleService;
	@Autowired
    private OrderService orderService;
	@Autowired
	private FileUploadService fileUploadService;

    @ModelAttribute
    private void userDetails(Model model, Principal principal){
        String username = principal.getName();
        UserDtls user = userRepository.findByUsername(username);
        List<Order> todayOrders = orderService.getOrdersByCurrentDate();
        model.addAttribute("todayOrdersNot",todayOrders.size());
        model.addAttribute("user",user);
		model.addAttribute("userRole",user.getRole());
    }

	@GetMapping("/")
	public String home(Model model) {
		List<UserDtls> users = userService.getUserDtls();
		List<Product> products = productRepository.findAll();
		List<Order> orders = orderRepository.findAll();
		List<Order> todayOrders = orderService.getOrdersByCurrentDate();
		List<OrderItem> orderItems = orderItemRepository.findAll();
		List<Customer> customers = customerRepository.findAll();
		int todayOrderAmount = todayOrders.stream()
		        .mapToInt(Order::getOrderAmount)
		        .sum();
		int totalOrderAmount = orders.stream()
				.mapToInt(Order::getOrderAmount)
				.sum();
		float orderProductQuantity = (float) orderItems.stream()
                .mapToDouble(OrderItem::getQuantity)
                .sum();
		float storeProductQuantity = (float) products.stream()
                .mapToDouble(Product::getQuantity)
                .sum();
		float totalProductQuantity = orderProductQuantity + storeProductQuantity;
		model.addAttribute("todayOrders",todayOrders.size());
		model.addAttribute("numOfUsers",users.size());
		model.addAttribute("numOfOrders",orders.size());
		model.addAttribute("numOfProducts",products.size());
		model.addAttribute("numOfCustomers",customers.size());
		model.addAttribute("todayOrderAmount",todayOrderAmount);
		model.addAttribute("totalOrderAmount",totalOrderAmount);
		model.addAttribute("totalProductQuantity",totalProductQuantity);
		return "admin/dashboard";
	}
	
	@GetMapping("/customer_list")
	public String customerList(Model model) {
		List<Customer> customers = customerService.getCustomers();
		model.addAttribute("customers",customers);
		return "admin/customer_list";
	}
	
	@GetMapping("/user_list")
	public String userList(Model model) {
		List<UserDtls> users = userService.getUserDtls();
		model.addAttribute("users",users);
		return "admin/user_list";
	}
	@GetMapping("/product_list")
	public String productList(Model model) {
		List<Product> products = productRepository.findAll();
		for(Product product : products) {
			List<OrderItem> orderItems = orderItemRepository.findByProduct(product);
			float totalQuantity = (float) orderItems.stream()
                    .mapToDouble(OrderItem::getQuantity)
                    .sum();
			product.setQuantity(product.getQuantity() + totalQuantity);
		}
		model.addAttribute("products",products);
		return "admin/product_list";
	}
	
	@GetMapping("/vehicle_list")
	public String vehicleList(Model model) {
		List<Vehicle> vehicles = vehicleRepository.findAll();
		model.addAttribute("vehicles",vehicles);
		return "admin/vehicle_list";
	}
	
	//customer
	
	@GetMapping("/add_customer")
    public String addCustomer(HttpSession session, Model model) {
        String msg = (String) session.getAttribute("msg");
        if (msg != null) {
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }
        return "admin/add_customer";
    }
	
	@PostMapping("/createCustomer")
	public String createCustomer(@ModelAttribute Customer customer, HttpSession session){
        String msg = null;
        if (!customerService.checkUsername(customer.getUsername())) {
        	Customer custDetails = customerService.createCustomer(customer);
            if (custDetails != null) {
                msg = "Customer added Successfully!";
            } else {
                msg = "Something went wrong on the server!";
            }
        } else {
            msg = "Username already exists!";
        }
        session.setAttribute("msg", msg);
        return "redirect:/admin/add_customer";
    }
	
	@GetMapping("/customerEdit/{id}")
	public String editCustomer(@PathVariable("id") Long id, Model model) {
		Customer customer = customerRepository.findById(id).orElse(null);
        model.addAttribute("customer", customer);
        return "admin/edit_customer";
	}
	@GetMapping("/customerView/{id}")
	public String viewCustomer(@PathVariable("id") Long id, Model model) {
		Customer customer = customerRepository.findById(id).orElse(null);
		List<Order> custOrderList = orderRepository.findByCustomer(customer);
		int custTotalOrderAmount = custOrderList.stream()
		        .mapToInt(Order::getOrderAmount)
		        .sum();
		model.addAttribute("customer", customer);
		model.addAttribute("custTotalNumOfOrder", custOrderList.size());
		model.addAttribute("custTotalOrderAmount", custTotalOrderAmount);
		return "admin/view_customer";
	}
	@PostMapping("/updateCustomer")
    public String updateCustomer(@ModelAttribute Customer customer, Model model) {
		customerService.updateCustomer(customer);
        return "redirect:/admin/customer_list"; // redirect to the list page after updating
    }
	@GetMapping("/customerDelete/{id}")
	public String deleteCustomer(@PathVariable("id") Long id) {
		customerRepository.deleteById(id);
        return "redirect:/admin/customer_list";
	}
	
	//user
	
	@GetMapping("/add_user")
    public String addUser(HttpSession session, Model model) {
        String msg = (String) session.getAttribute("msg");
        if (msg != null) {
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }
        return "admin/add_user";
    }
    @PostMapping("/createUser")
    public String createUser(@ModelAttribute UserDtls user, @RequestParam("photo") MultipartFile photo, Model model, HttpSession session){
    	try {
            String msg = null;
            if (!userService.checkUsername(user.getUsername())) {
            	String fileName = fileUploadService.saveFile(photo,user.getUsername());
                System.out.println("fileName==Controlleer==>>>"+fileName);
                if (fileName != null) {
                    user.setPhotoPath(fileName);
                }
                UserDtls userDetails = userService.createUserByAdmin(user);
                if (userDetails != null) {
                    msg = "Register Successfully!";
                } else {
                    msg = "Something went wrong on the server!";
                }
            } else {
                msg = "Username already exists!";
            }
            session.setAttribute("msg", msg);
        } catch (IOException e) {
            model.addAttribute("error", "Error uploading file: " + e.getMessage());
            return "redirect:/admin/add_user";
        }
        return "redirect:/admin/add_user";
    }
	@GetMapping("/userEdit/{id}")
	public String editUser(@PathVariable("id") Long id, Model model) {
        UserDtls editUser = userService.findUserById(id);
        model.addAttribute("editUser", editUser);
        return "admin/edit_user";
	}
	@PostMapping("/updateUser")
    public String updateUser(@ModelAttribute UserDtls user, Model model) {
        userService.updateUserDtls(user);
        return "redirect:/admin/user_list"; // redirect to the list page after updating
    }
	@GetMapping("/userDelete/{id}")
	public String deleteUser(@PathVariable("id") Long id) {
		userRepository.deleteById(id);
        return "redirect:/admin/user_list";
	}
	
	//product
	@GetMapping("/add_product")
    public String addProduct(HttpSession session, Model model) {
        String msg = (String) session.getAttribute("msg");
        if (msg != null) {
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }
        return "admin/add_product";
    }
    @PostMapping("/createProduct")
    public String createProduct(@ModelAttribute Product product, HttpSession session){
        String msg = null;
        if (!productService.checkShortName(product.getShortName())) {
        	Product productDetails = productRepository.save(product);
            if (productDetails != null) {
                msg = "Register Successfully!";
            } else {
                msg = "Something went wrong on the server!";
            }
        } else {
            msg = "Short Name already exists!";
        }
        session.setAttribute("msg", msg);
        return "redirect:/admin/add_product";
    }
	@GetMapping("/productEdit/{id}")
	public String editProduct(@PathVariable("id") Long id, Model model) {
		Product product = productRepository.findById(id).orElse(null);
        model.addAttribute("product", product);
        return "admin/edit_product";
	}
	@PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product, Model model) {
		productRepository.save(product);
        return "redirect:/admin/product_list"; // redirect to the list page after updating
    }
	@GetMapping("/productDelete/{id}")
	public String deleteProduct(@PathVariable("id") Long id) {
		productRepository.deleteById(id);
        return "redirect:/admin/product_list";
	}
	
	//vehicle
	@GetMapping("/add_vehicle")
    public String addVehicle(HttpSession session, Model model) {
        String msg = (String) session.getAttribute("msg");
        if (msg != null) {
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }
        return "admin/add_vehicle";
    }
    @PostMapping("/createVehicle")
    public String createVehicle(@ModelAttribute Vehicle vehicle, HttpSession session){
        String msg = null;
        if (!vehicleService.existsByNumberPlate(vehicle.getNumberPlate())) {
        	Vehicle vehicleDetails = vehicleRepository.save(vehicle);
            if (vehicleDetails != null) {
                msg = "Register Successfully!";
            } else {
                msg = "Something went wrong on the server!";
            }
        } else {
            msg = "Number Plate already exists!";
        }
        session.setAttribute("msg", msg);
        return "redirect:/admin/add_vehicle";
    }
	@GetMapping("/vehicleEdit/{id}")
	public String editVehicle(@PathVariable("id") Long id, Model model) {
		Vehicle vehicle = vehicleRepository.findById(id).orElse(null);
        model.addAttribute("vehicle", vehicle);
        return "admin/edit_vehicle";
	}
	@PostMapping("/updateVehicle")
    public String updateVehicle(@ModelAttribute Vehicle vehicle, Model model) {
		vehicleRepository.save(vehicle);
        return "redirect:/admin/vehicle_list"; // redirect to the list page after updating
    }
	@GetMapping("/vehicleDelete/{id}")
	public String deleteVehicle(@PathVariable("id") Long id) {
		vehicleRepository.deleteById(id);
        return "redirect:/admin/vehicle_list";
	}
}

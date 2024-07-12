package com.ahsan.scrap.controller.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
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

import com.ahsan.scrap.model.Customer;
import com.ahsan.scrap.model.Order;
import com.ahsan.scrap.model.OrderItem;
import com.ahsan.scrap.model.Product;
import com.ahsan.scrap.model.UserDtls;
import com.ahsan.scrap.model.Vehicle;
import com.ahsan.scrap.repository.CustomerRepository;
import com.ahsan.scrap.repository.OrderRepository;
import com.ahsan.scrap.repository.ProductRepository;
import com.ahsan.scrap.repository.UserRepository;
import com.ahsan.scrap.repository.VehicleRepository;
import com.ahsan.scrap.service.CustomerService;
import com.ahsan.scrap.service.OrderService;
import com.ahsan.scrap.service.ProductService;
import com.ahsan.scrap.service.UserService;
import com.ahsan.scrap.service.VehicleService;

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
	private OrderRepository orderRepository;
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

    @ModelAttribute
    private void userDetails(Model model, Principal principal){
        String username = principal.getName();
        UserDtls user = userRepository.findByUsername(username);
        model.addAttribute("user",user);
    }

	@GetMapping("/")
	public String home() {
		return "admin/home";
	}
	
	@GetMapping("/profile")
	public String prfile() {
		return "admin/profile";
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
//        if(result.hasErrors()) {
//        	return "admin/add_customer";
//        }
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
    public String createUser(@ModelAttribute UserDtls user, HttpSession session){
        String msg = null;
        if (!userService.checkUsername(user.getUsername())) {
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
        return "redirect:/admin/add_user";
    }
	@GetMapping("/userEdit/{id}")
	public String editUser(@PathVariable("id") Long id, Model model) {
        UserDtls user = userService.findUserById(id);
        model.addAttribute("user", user);
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
	
	//oder 
	

//	@GetMapping("/new")
//	public String showOrderForm(Model model, Principal principal) {
//	    Order order = new Order();
//	    order.setOrderDate(LocalDate.now());
//	    order.setOrderItems(new ArrayList<>());
//
//	    // Assuming UserDtls can be fetched from the principal
//	    // Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//	    // if (principalObject instanceof UserDtls) {
//	    //     UserDtls userDtls = (UserDtls) principalObject;
//	    //     order.setUserDtls(userDtls);
//	    // }
//	    List<UserDtls> userDtls = userRepository.findAll();
//	    if (userDtls.isEmpty()) {
//	        System.out.println("userDtls list is empty");
//	    } else {
//	        System.out.println("userDtls list size: " + userDtls);
//	    }
////	    order.setUserDtls(userDtls);
//	    List<Customer> customers = orderService.getAllCustomers();
//	    if (customers.isEmpty()) {
//	        System.out.println("Customer list is empty");
//	    } else {
//	        System.out.println("Customer list size: " + customers);
//	    }
//
//	    model.addAttribute("order", order);
//	    model.addAttribute("userDtls", userDtls);
//	    model.addAttribute("customers", customers);
//	    model.addAttribute("products", orderService.getAllProducts());
//
//	    return "admin/orderForm";
//	}
//
//    @PostMapping("/saveOrder")
//    public String saveOrder(@ModelAttribute Order order, @RequestParam("customerId") Long customerId,
//            @RequestParam("userDtlsId") Long userDtlsId) {
//    	// Fetch and set the Customer object
//        Customer customer = customerRepository.findById(customerId).orElse(null);
//        order.setCustomer(customer);
//
//        // Fetch and set the UserDtls object
//        UserDtls userDtls = userRepository.findById(userDtlsId).orElse(null);
//        order.setUserDtls(userDtls);
//    	System.out.println("customerId====>>>"+customerId);
//    	System.out.println("userDtlsId====>>>"+userDtlsId);
//    	System.out.println("order====>>>"+order);
//        orderService.saveOrder(order);
//        return "redirect:/admin/orderForm";
//    }
	
	@GetMapping("/order_list")
	public String orderList(Model model) {
		List<Order> orders = orderService.getOrdersByOrderDateDesc();
		model.addAttribute("orders",orders);
		return "admin/order_list";
	}
	
	@GetMapping("/orderForm")
    public String showOrderForm(Model model, Principal principal) {
        Order order = new Order();
        order.setOrderItems(new ArrayList<>(Collections.singletonList(new OrderItem()))); // Initialize with one item
        List<Customer> customers = customerRepository.findAll();

        model.addAttribute("order", order);
        model.addAttribute("customers", customers);
        model.addAttribute("products", orderService.getAllProducts());

        return "admin/orderForm";
    }

    @PostMapping("/saveOrder")
    public String saveOrder(@ModelAttribute Order order, @RequestParam("customerId") Long customerId) {
        orderService.saveOrder(order, customerId);
        return "redirect:/admin/orderForm";
    }
    @GetMapping("/orderEdit/{id}")
	public String editOrder(@PathVariable("id") Long id, Model model) {
		Order order = orderRepository.findById(id).orElse(null);
        model.addAttribute("order", order);
        return "admin/edit_order";
	}
	@PostMapping("/updateOrder")
    public String updateOrder(@ModelAttribute Order order, Model model) {
		orderRepository.save(order);
        return "redirect:/admin/order_list"; // redirect to the list page after updating
    }
	@GetMapping("/orderDelete/{id}")
	public String deleteOrder(@PathVariable("id") Long id) {
		orderRepository.deleteById(id);
        return "redirect:/admin/order_list";
	}
	
}

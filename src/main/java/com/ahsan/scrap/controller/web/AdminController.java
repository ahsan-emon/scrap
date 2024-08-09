package com.ahsan.scrap.controller.web;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

import com.ahsan.scrap.constraint.CommonConstraint;
import com.ahsan.scrap.dto.UserDto;
import com.ahsan.scrap.exception.UserNotAuthenticatedException;
import com.ahsan.scrap.model.AssignEmployee;
import com.ahsan.scrap.model.Customer;
import com.ahsan.scrap.model.Expense;
import com.ahsan.scrap.model.Order;
import com.ahsan.scrap.model.OrderItem;
import com.ahsan.scrap.model.OrderRequest;
import com.ahsan.scrap.model.Product;
import com.ahsan.scrap.model.Sell;
import com.ahsan.scrap.model.UserDtls;
import com.ahsan.scrap.model.Vehicle;
import com.ahsan.scrap.repository.AssignEmployeeRepository;
import com.ahsan.scrap.repository.CustomerRepository;
import com.ahsan.scrap.repository.ExpenseRepository;
import com.ahsan.scrap.repository.OrderItemRepository;
import com.ahsan.scrap.repository.OrderRepository;
import com.ahsan.scrap.repository.OrderRequestRepository;
import com.ahsan.scrap.repository.ProductRepository;
import com.ahsan.scrap.repository.SellRepository;
import com.ahsan.scrap.repository.UserRepository;
import com.ahsan.scrap.repository.VehicleRepository;
import com.ahsan.scrap.service.AssignEmployeeService;
import com.ahsan.scrap.service.CustomerService;
import com.ahsan.scrap.service.ExpenseService;
import com.ahsan.scrap.service.OrderRequestService;
import com.ahsan.scrap.service.OrderService;
import com.ahsan.scrap.service.ProductService;
import com.ahsan.scrap.service.SellService;
import com.ahsan.scrap.service.UserService;
import com.ahsan.scrap.service.VehicleService;
import com.ahsan.scrap.util.FileUploadService;
import com.ahsan.scrap.util.UserUtil;

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
	private OrderItemRepository orderItemRepository;
	@Autowired
	private SellRepository sellRepository;
	@Autowired
	private AssignEmployeeRepository assignEmployeeRepository;
	@Autowired
	private ExpenseRepository expenseRepository;
	@Autowired
	private OrderRequestRepository orderRequestRepository;
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
	private SellService sellService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private AssignEmployeeService assignEmployeeService;
	@Autowired
	private ExpenseService expenseService;
	@Autowired
	private OrderRequestService orderRequestService;

    @ModelAttribute
    private void userDetails(Model model, Principal principal){
    	if(principal != null) {    
    		String username = principal.getName();
	        UserDtls user = userRepository.findByUsername(username);
	        List<Order> todayOrders = orderService.getOrdersByCurrentDate();
	        List<OrderRequest> orderRequestMessageList = orderRequestService.getOrderRequestsByCurrentDateDesc();
	        model.addAttribute("todayOrdersNot",todayOrders.size());
	        model.addAttribute("orderRequestMessageList",orderRequestMessageList);
	        model.addAttribute("orderRequestMessage",orderRequestMessageList.size());
	        model.addAttribute("user",user);
			model.addAttribute("userRole",user.getRole());
	    }else {
	    	throw new UserNotAuthenticatedException("User is not authenticated");
	    }
    }

	@GetMapping("/")
	public String home(Model model) {
		String username = UserUtil.getCurrentUsername();
		UserDtls user = userRepository.findByUsername(username);
		if(user != null && user.getRole().equals(CommonConstraint.ROLE_ADMIN)) {   
			List<UserDtls> users = userService.getUserDtls();
			List<Product> products = productRepository.findAll();
			List<Order> orders = orderRepository.findAll();
	//		List<Order> todayOrders = orderService.getOrdersByCurrentDate();
			List<Order> todayOrders = orderService.getOrdersWithinDateTimeRange();
			List<AssignEmployee> assignList = assignEmployeeRepository.findAll();
			List<AssignEmployee> assignListWithoutOwner = new ArrayList<AssignEmployee>();
			for(AssignEmployee assignEmp : assignList) {
				if(!assignEmp.getUserDtls().getRole().equals(CommonConstraint.ROLE_OWNER)) {
					assignListWithoutOwner.add(assignEmp);
				}
			}
			List<AssignEmployee> todayAssignList = assignEmployeeService.getAssignsWithinCurrentDateTimeRange();
			List<AssignEmployee> todayAssignListWithoutOwner = new ArrayList<AssignEmployee>();
			for(AssignEmployee assignEmp : todayAssignList) {
				if(!assignEmp.getUserDtls().getRole().equals(CommonConstraint.ROLE_OWNER)) {
					todayAssignListWithoutOwner.add(assignEmp);
				}
			}
			List<Expense> expenses = expenseRepository.findAll();
			List<Expense> todayExpenses = expenseService.getExpensesWithinCurrentDateTimeRange();
			List<Customer> customers = customerRepository.findAll();
			List<Vehicle> vehicles = vehicleRepository.findAll();
			
			//sell 
			List<Sell> sells = sellRepository.findAll();
			List<Sell> todaySells = sellService.getSellsByCurrentDate();
			
			//sell calculation
			float todaySellAmount = (float) todaySells.stream()
			        .mapToDouble(Sell::getSellAmount)
			        .sum();
			todaySellAmount = Float.parseFloat(String.format("%.2f", todaySellAmount));
			float todaySellProdQuantity = (float) todaySells.stream()
					.mapToDouble(Sell::getSellQuantity)
					.sum();
			todaySellProdQuantity = Float.parseFloat(String.format("%.2f", todaySellProdQuantity));
			float totalSellAmount = (float) sells.stream()
					.mapToDouble(Sell::getSellAmount)
					.sum();
			totalSellAmount = Float.parseFloat(String.format("%.2f", totalSellAmount));
			float totalSellProdQuantity = (float) sells.stream()
					.mapToDouble(Sell::getSellQuantity)
					.sum();
			totalSellProdQuantity = Float.parseFloat(String.format("%.2f", totalSellProdQuantity));
			model.addAttribute("numOfTodaySells",todaySells.size());
			model.addAttribute("numOfSells",sells.size());
			model.addAttribute("todaySellAmount",todaySellAmount);
			model.addAttribute("todaySellProdQuantity",todaySellProdQuantity);
			model.addAttribute("totalSellAmount",totalSellAmount);
			model.addAttribute("totalSellProdQuantity",totalSellProdQuantity);
			
			int todayOrderAmount = todayOrders.stream()
			        .mapToInt(Order::getOrderAmount)
			        .sum();
			float todayOrderQuantity = (float) todayOrders.stream()
	                .mapToDouble(Order::getOrderQuantity)
	                .sum();
			todayOrderQuantity = Float.parseFloat(String.format("%.2f", todayOrderQuantity));
			int totalOrderAmount = orders.stream()
					.mapToInt(Order::getOrderAmount)
					.sum();
			float orderProductQuantity = (float) orders.stream()
	                .mapToDouble(Order::getOrderQuantity)
	                .sum();
			orderProductQuantity = Float.parseFloat(String.format("%.2f", orderProductQuantity));
			float storeProductQuantity = (float) products.stream()
	                .mapToDouble(Product::getQuantity)
	                .sum();
			storeProductQuantity = Float.parseFloat(String.format("%.2f", storeProductQuantity));
			float totalProductQuantity = orderProductQuantity + storeProductQuantity - totalSellProdQuantity;
			totalProductQuantity = Float.parseFloat(String.format("%.2f", totalProductQuantity));
			int todayAssignAmount = todayAssignListWithoutOwner.stream()
			        .mapToInt(AssignEmployee::getAssignAmount)
			        .sum();
			int totalAssignAmount = assignListWithoutOwner.stream()
					.mapToInt(AssignEmployee::getAssignAmount)
					.sum();
			int todayExpenseAmount = todayExpenses.stream()
					.mapToInt(Expense::getExpenseAmount)
					.sum();
			int totalExpenseAmount = expenses.stream()
					.mapToInt(Expense::getExpenseAmount)
					.sum();
			//totalCompanyAmount
			int totalCompanyAmount = 0;
			UserDtls ownerUser = userRepository.findByRole(CommonConstraint.ROLE_OWNER);
			if(ownerUser != null) {
				List<AssignEmployee> ownerAssignList = assignEmployeeService.getAssignsByUserDtls(ownerUser);
				int ownerTotalAssignAmount = ownerAssignList.stream()
				        .mapToInt(AssignEmployee::getAssignAmount)
				        .sum();
//				totalCompanyAmount = ownerTotalAssignAmount + (int) totalSellAmount - totalOrderAmount - totalExpenseAmount;
				totalCompanyAmount = ownerTotalAssignAmount + (int) totalSellAmount - totalAssignAmount;
			}
			//totalAmountEmployeeHas
			int totalAmountEmployeeHas = totalAssignAmount - totalOrderAmount - totalExpenseAmount;
			model.addAttribute("todayOrders",todayOrders.size());
			model.addAttribute("numOfUsers",users.size());
			model.addAttribute("numOfOrders",orders.size());
			model.addAttribute("numOfProducts",products.size());
			model.addAttribute("numOfCustomers",customers.size());
			model.addAttribute("numOfVehicles",vehicles.size());
			model.addAttribute("todayOrderAmount",todayOrderAmount);
			model.addAttribute("todayOrderQuantity",todayOrderQuantity);
			model.addAttribute("totalOrderAmount",totalOrderAmount);
			model.addAttribute("totalProductQuantity",totalProductQuantity);
			model.addAttribute("todayAssignAmount",todayAssignAmount);
			model.addAttribute("totalAssignAmount",totalAssignAmount);
			model.addAttribute("todayExpenseAmount",todayExpenseAmount);
			model.addAttribute("totalExpenseAmount",totalExpenseAmount);
			model.addAttribute("totalCompanyAmount",totalCompanyAmount);
			model.addAttribute("totalAmountEmployeeHas",totalAmountEmployeeHas);
			return "admin/dashboard";
		}else {
	    	throw new UserNotAuthenticatedException("User is not authenticated");
	    }
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
		List<UserDto> userDtos = new ArrayList<>();
		for(UserDtls user : users) {
			List<AssignEmployee> userWiseAssignList = assignEmployeeService.getAssignsByUserDtls(user);
			List<Order> userWiseOrders = orderService.findByUserDtls(user);
			List<Expense> userWiseExpenses = expenseService.getExpensesByUserDtls(user);
			List<Sell> sells = sellRepository.findAll();
			List<AssignEmployee> assignList = assignEmployeeRepository.findAll();
			List<AssignEmployee> assignListWithoutOwner = new ArrayList<AssignEmployee>();
			for(AssignEmployee assignEmp : assignList) {
				if(!assignEmp.getUserDtls().getRole().equals(CommonConstraint.ROLE_OWNER)) {
					assignListWithoutOwner.add(assignEmp);
				}
			}
			
			int totalAssignAmount = assignListWithoutOwner.stream()
					.mapToInt(AssignEmployee::getAssignAmount)
					.sum();
			float totalSellAmount = (float) sells.stream()
					.mapToDouble(Sell::getSellAmount)
					.sum();
			totalSellAmount = Float.parseFloat(String.format("%.2f", totalSellAmount));
			int userTotalAssignAmount = userWiseAssignList.stream()
					.mapToInt(AssignEmployee::getAssignAmount)
					.sum();
			int userTotalOrderAmount = userWiseOrders.stream()
					.mapToInt(Order::getOrderAmount)
					.sum();
			int userTotalExpenseAmount = userWiseExpenses.stream()
					.mapToInt(Expense::getExpenseAmount)
					.sum();
			int hasAmount = 0;
			if(user.getRole().equals(CommonConstraint.ROLE_OWNER)) {
				hasAmount = userTotalAssignAmount + (int) totalSellAmount - totalAssignAmount;
			}else {
				hasAmount = userTotalAssignAmount - userTotalOrderAmount - userTotalExpenseAmount;
			}
			UserDto userDto = new UserDto(user, hasAmount);
			userDtos.add(userDto);
		}
		model.addAttribute("users",userDtos);
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
            	String previousFileName = "";
				if(user.getId() != null) {
					UserDtls oldUser = userRepository.findById(user.getId()).orElse(null);
					if(oldUser != null && oldUser.getPhotoPath() != null) {
						previousFileName = oldUser.getPhotoPath();
					}
				}
				String fileName = fileUploadService.saveFile(photo,user.getUsername(),previousFileName);
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
    @GetMapping("/userView/{id}")
	public String viewUser(@PathVariable("id") Long id, Model model) {
    	UserDtls viewUser = userRepository.findById(id).orElse(null);
		List<Order> userOrderList = orderRepository.findByUserDtls(viewUser);
		List<Expense> totalExpenses = expenseService.getExpensesByUserDtls(viewUser);
		List<AssignEmployee> userAssignList = assignEmployeeService.getAssignsByUserDtls(viewUser);
		String vehicleName = "";
		if(viewUser.getVehicleId() != null) {
			Vehicle vehicle = vehicleRepository.findById(viewUser.getVehicleId()).orElse(null);
			if(vehicle != null) {
				vehicleName = vehicle.getModel();
			}
		}
		int userTotalOrderAmount = userOrderList.stream()
		        .mapToInt(Order::getOrderAmount)
		        .sum();
		int totalAssignAmount = userAssignList.stream()
				.mapToInt(AssignEmployee::getAssignAmount)
				.sum();
		int totalExpenseAmount = totalExpenses.stream()
				.mapToInt(Expense::getExpenseAmount)
				.sum();
		int hasAmount = totalAssignAmount - userTotalOrderAmount - totalExpenseAmount;
		model.addAttribute("viewUser", viewUser);
		model.addAttribute("vehicleName", vehicleName);
		model.addAttribute("userTotalNumOfOrder", userOrderList.size());
		model.addAttribute("userTotalOrderAmount", userTotalOrderAmount);
		model.addAttribute("hasAmount", hasAmount);
		return "admin/view_user";
	}
	@GetMapping("/userEdit/{id}")
	public String editUser(@PathVariable("id") Long id, Model model) {
        UserDtls editUser = userService.findUserById(id);
        model.addAttribute("editUser", editUser);
        return "admin/edit_user";
	}
	@PostMapping("/updateUser")
    public String updateUser(@ModelAttribute UserDtls user, Model model, @RequestParam("photo") MultipartFile photo) {
		try {
			String previousFileName = "";
			if(user.getId() != null) {
				UserDtls oldUser = userRepository.findById(user.getId()).orElse(null);
				if(oldUser != null && oldUser.getPhotoPath() != null) {
					previousFileName = oldUser.getPhotoPath();
				}
			}
			String fileName = fileUploadService.saveFile(photo,user.getUsername(),previousFileName);
			if (fileName != null) {
                user.setPhotoPath(fileName);
            }
			userService.updateUserDtls(user);
		} catch (IOException e) {
			model.addAttribute("error", "Error uploading file: " + e.getMessage());
            return "redirect:/admin/edit_user";
		}
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
        	product.setUpdateDate(LocalDateTime.now());
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
	@GetMapping("/productView/{id}")
	public String viewProduct(@PathVariable("id") Long id, Model model) {
		Product product = productRepository.findById(id).orElse(null);
		List<OrderItem> prodOrderItemList = orderItemRepository.findByProduct(product);
		float totalOrderQuantity = (float) prodOrderItemList.stream()
                .mapToDouble(OrderItem::getQuantity)
                .sum();
		int totalOrderBuyingAmount = prodOrderItemList.stream()
                .mapToInt(OrderItem::getAmount)
                .sum();
		//total order buying amount with previous store buying (previous quantity multiply with current unit price)
		int totalBuyingAmount = totalOrderBuyingAmount + (int) ( product.getQuantity() * product.getUnitPrice());
		float currentQuantityInStore = totalOrderQuantity + product.getQuantity();
		model.addAttribute("currentQuantityInStore", currentQuantityInStore);
		model.addAttribute("totalBuyingAmount", totalBuyingAmount);
		model.addAttribute("product", product);
		return "admin/view_product";
	}
	@PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product, Model model) {
		product.setUpdateDate(LocalDateTime.now());
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
	//assign money car to employee
	@GetMapping("/assignList")
    public String assignList(Model model) {
		List<AssignEmployee> assignEmployees = assignEmployeeService.getAssignEmployeesByAssignDateDesc();
        model.addAttribute("assignEmployees", assignEmployees);
        return "admin/assign_list";
    }
	@GetMapping("/todayAssignList")
	public String todayAssignList(Model model) {
		List<AssignEmployee> assignEmployees = assignEmployeeService.getAssignsWithinCurrentDateTimeRange();
		model.addAttribute("assignEmployees", assignEmployees);
		return "admin/assign_list";
	}
	@GetMapping("/assignToEmployee")
	public String assignToEmployee(HttpSession session, Model model) {
		String msg = (String) session.getAttribute("msg");
		if (msg != null) {
			model.addAttribute("msg", msg);
			session.removeAttribute("msg");
		}
		List<UserDtls> users = userRepository.findAll();
		List<UserDtls> employees = new ArrayList<>();
		for(UserDtls emp : users) {
			if(emp.getRole().equals(CommonConstraint.ROLE_ADMIN) || emp.getRole().equals(CommonConstraint.ROLE_EMPLOYEE) || emp.getRole().equals(CommonConstraint.ROLE_OWNER)) {
				employees.add(emp);
			}
		}
		List<Vehicle> vehicles = vehicleRepository.findAll();
		model.addAttribute("employees", employees);
		model.addAttribute("vehicles", vehicles);
		return "admin/assignToEmp";
	}
	@PostMapping("/addAssign")
    public String addAssign(@ModelAttribute AssignEmployee assignEmployee, @RequestParam("userDtlsId") Long userDtlsId, @RequestParam(name = "vehicleId", required = false) Long vehicleId, HttpSession session){
		String msg = null;
		AssignEmployee assignEmp = assignEmployeeService.saveAssign(assignEmployee, userDtlsId, vehicleId);
        if(assignEmp != null) {
        	msg = "Assign Successfully!";
        }else {
        	msg = "Something went wrong on the server!";
        }
        session.setAttribute("msg", msg);
        return "redirect:/admin/assignToEmployee";
    }
	@GetMapping("/assignEdit/{id}")
	public String editAssign(@PathVariable("id") Long id, Model model) {
		AssignEmployee assignEmployee = assignEmployeeRepository.findById(id).orElse(null);
		List<UserDtls> users = userRepository.findAll();
		List<UserDtls> employees = new ArrayList<>();
		for(UserDtls emp : users) {
			if(emp.getRole().equals(CommonConstraint.ROLE_ADMIN) || emp.getRole().equals(CommonConstraint.ROLE_EMPLOYEE)) {
				employees.add(emp);
			}
		}
		List<Vehicle> vehicles = vehicleRepository.findAll();
		model.addAttribute("employees", employees);
		model.addAttribute("vehicles", vehicles);
        model.addAttribute("assignEmployee", assignEmployee);
        return "admin/edit_assign";
	}
	@PostMapping("/updateAssign")
    public String updateAssign(@ModelAttribute AssignEmployee assignEmployee, @RequestParam("userDtlsId") Long userDtlsId, @RequestParam(name = "vehicleId", required = false) Long vehicleId) {
		assignEmployeeService.saveAssign(assignEmployee, userDtlsId, vehicleId);
		return "redirect:/admin/assignList"; // redirect to the list page after updating
    }
	@GetMapping("/assignDelete/{id}")
	public String deleteAssign(@PathVariable("id") Long id) {
		assignEmployeeRepository.deleteById(id);
        return "redirect:/admin/assignList";
	}
	
//	user order list
	@GetMapping("/userOrderList/{id}")
	public String userOrderList(@PathVariable("id") Long id, Model model) {
        UserDtls userDtls = userRepository.findById(id).orElse(null);
		List<Order> orders = orderRepository.findByUserDtls(userDtls);
		List<Customer> customers = customerRepository.findAll();
        List<UserDtls> userList = userRepository.findAll();
        model.addAttribute("userId", userDtls.getId());
        model.addAttribute("customers", customers);
        model.addAttribute("userList", userList);
		model.addAttribute("orders",orders);
		return "order/order_list";
	}
	//order request 

    @GetMapping("/orderRequestList")
    public String orderRequestList(Model model){
    	List<OrderRequest> orderRequests = orderRequestRepository.findAllByOrderByRequestDateDesc();
    	model.addAttribute("orderRequests", orderRequests);
        return "admin/orderRequest_list";
    }
    @GetMapping("/todayOrderRequestList")
    public String todayOrderRequestList(Model model){
    	List<OrderRequest> orderRequests = orderRequestService.getOrderRequestsByCurrentDateDesc();
    	model.addAttribute("orderRequests", orderRequests);
    	return "admin/orderRequest_list";
    }
    @GetMapping("/orderRequestDelete/{id}")
	public String deleteOrderRequest(@PathVariable("id") Long id) {
    	orderRequestRepository.deleteById(id);
        return "redirect:/admin/orderRequestList";
	}
}

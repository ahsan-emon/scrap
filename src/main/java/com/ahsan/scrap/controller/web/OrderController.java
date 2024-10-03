package com.ahsan.scrap.controller.web;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ahsan.scrap.constraint.CommonConstraint;
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
import com.ahsan.scrap.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
    private OrderService orderService;
	@Autowired
    private CustomerRepository customerRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@ModelAttribute
    private void userDetails(Model model, Principal principal){
        String username = principal.getName();
        UserDtls user = userRepository.findByUsername(username);
        model.addAttribute("userRole",user.getRole());
        model.addAttribute("user",user);
    }
	
	//oder 
	
//		@GetMapping("/order_list")
//		public String orderList(Model model, Principal principal) {
//	        String username = principal.getName();
//	        UserDtls userDtls = userRepository.findByUsername(username);
//	        if(userDtls.getRole().equals(CommonConstraint.ROLE_ADMIN)) {
//				List<Order> orders = orderService.getOrdersByOrderDateDesc();
//				model.addAttribute("orders",orders);
//	        }else {
//	        	List<Order> orders = orderService.getOrdersByUserDtlsAndUptoPrevOrderDate(userDtls);
//				model.addAttribute("orders",orders);
//	        }
//			return "order/order_list";
//		}
		
		
		//order list with search mechanism
		@GetMapping("/order_list")
		public String orderList(
		    @RequestParam(name = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
		    @RequestParam(name = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
		    @RequestParam(name = "customerId", required = false) Long customerId,
		    @RequestParam(name = "userId", required = false) Long userId,
		    Model model,
		    Principal principal) {
	
		    String username = principal.getName();
		    UserDtls userDtls = userRepository.findByUsername(username);
		    
		    List<Order> orders;
		    int ordersAmount = 0;
		    float ordersQuantity = 0f;
		    if (userDtls.getRole().equals(CommonConstraint.ROLE_ADMIN)) {
		        orders = orderService.searchOrders(fromDate, toDate, customerId, userId);
		        ordersAmount = orders.stream()
						.mapToInt(Order::getOrderAmount)
						.sum();
				ordersQuantity = (float) orders.stream()
		                .mapToDouble(Order::getOrderQuantity)
		                .sum();
		    } else {
		        orders = orderService.getOrdersByUserDtlsAndUptoPrevOrderDate(userDtls);
		    }
	
		    model.addAttribute("orders", orders);
	
		    if (userDtls.getRole().equals(CommonConstraint.ROLE_ADMIN)) {
		        List<Customer> customers = customerRepository.findAll();
		        List<UserDtls> userList = userRepository.findAll();
		        model.addAttribute("fromDate", fromDate);
			    model.addAttribute("toDate", toDate);
		        model.addAttribute("userId", userId);
		        model.addAttribute("customerId", customerId);
		        model.addAttribute("customers", customers);
		        model.addAttribute("userList", userList);
		        model.addAttribute("ordersAmount", ordersAmount);
		        model.addAttribute("ordersQuantity", ordersQuantity);
		    }
	
		    return "order/order_list";
		}

		
		@GetMapping("/today_order_list")
		public String todayOrderList(Model model, Principal principal) {
			String username = principal.getName();
			UserDtls userDtls = userRepository.findByUsername(username);
			if(userDtls.getRole().equals(CommonConstraint.ROLE_ADMIN)) {
//				List<Order> todayOrders = orderService.getOrdersByCurrentDate();
				List<Order> todayOrders = orderService.getOrdersWithinDateTimeRange();
				model.addAttribute("orders",todayOrders);
			}else {
//				List<Order> orders = orderService.getOrdersByUserDtlsAndCurrentDate(userDtls);
				List<Order> ordersForUser = orderService.getOrdersByUserDtlsAndCurrentDateTimeRange(userDtls);
				model.addAttribute("orders",ordersForUser);
			}
			return "order/order_list";
		}
		
		@GetMapping("/orderForm")
	    public String showOrderForm(Model model, Principal principal) {
	        Order order = new Order();
	        order.setOrderItems(new ArrayList<>(Collections.singletonList(new OrderItem()))); // Initialize with one item
	        List<Customer> customers = customerRepository.findAll();

	        model.addAttribute("order", order);
	        model.addAttribute("customers", customers);
	        model.addAttribute("products", orderService.getAllProducts());

	        return "order/orderForm";
	    }

	    @PostMapping("/saveOrder")
	    public String saveOrder(@ModelAttribute Order order, @RequestParam("customerId") Long customerId, @RequestParam("customerDue") int customerDue) {
	        orderService.saveOrder(order, customerId);
	        Customer customer = order.getCustomer();
	        if(customer != null) {
	        	customer.setCustomerDue(customerDue);
	        	customerRepository.save(customer);
	        }
	        return "redirect:/order/order_list";
	    }
	    @GetMapping("/orderView/{id}")
	   	public String viewOrder(@PathVariable("id") Long id, Model model) {
	   		Order order = orderRepository.findById(id).orElse(null);
	   		String vehicleName = "";
	   		if(order.getVehicleId() != null) {
	   			Vehicle vehicle = vehicleRepository.findById(order.getVehicleId()).orElse(null);
	   			vehicleName = vehicle.getModel() + "(" + vehicle.getNumberPlate() + ")";
	   		}
	        model.addAttribute("order", order);
	        model.addAttribute("vehicleName", vehicleName);
	        return "order/view_order";
	   	}
	    @GetMapping("/orderEdit/{id}")
		public String editOrder(@PathVariable("id") Long id, Model model) {
			Order order = orderRepository.findById(id).orElse(null);
			List<Product> products = productRepository.findAll();
	        model.addAttribute("order", order);
	        model.addAttribute("products", products);
	        return "order/edit_order";
		}
		@PostMapping("/updateOrder")
	    public String updateOrder(@ModelAttribute Order order, @RequestParam("customerDue") int customerDue) {
			orderService.updateOrder(order);
			Customer customer = order.getCustomer();
	        if(customer != null) {
	        	customer.setCustomerDue(customerDue);
	        	customerRepository.save(customer);
	        }
	        return "redirect:/order/order_list";
	    }
		@GetMapping("/orderDelete/{id}")
		public String deleteOrder(@PathVariable("id") Long id) {
			orderRepository.deleteById(id);
	        return "redirect:/order/order_list";
		}
}

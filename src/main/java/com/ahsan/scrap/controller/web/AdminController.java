package com.ahsan.scrap.controller.web;

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

import com.ahsan.scrap.model.Customer;
import com.ahsan.scrap.model.UserDtls;
import com.ahsan.scrap.repository.CustomerRepository;
import com.ahsan.scrap.repository.UserRepository;
import com.ahsan.scrap.service.CustomerService;
import com.ahsan.scrap.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
    private UserRepository userRepository;
	@Autowired
    private CustomerRepository customerRepository;
	@Autowired
    private CustomerService customerService;
	@Autowired
    private UserService userService;

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
    public String createUser(@ModelAttribute Customer cust, HttpSession session){
        String msg = null;
        if (!customerService.checkUsername(cust.getUsername())) {
        	Customer custDetails = customerService.createCustomer(cust);
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
        return "admin/editCustomer";
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
        return "admin/editUser";
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
	
}

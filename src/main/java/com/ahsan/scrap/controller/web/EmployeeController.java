package com.ahsan.scrap.controller.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ahsan.scrap.exception.UserNotAuthenticatedException;
import com.ahsan.scrap.model.AssignEmployee;
import com.ahsan.scrap.model.Expense;
import com.ahsan.scrap.model.Order;
import com.ahsan.scrap.model.UserDtls;
import com.ahsan.scrap.repository.UserRepository;
import com.ahsan.scrap.service.AssignEmployeeService;
import com.ahsan.scrap.service.ExpenseService;
import com.ahsan.scrap.service.OrderService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
	@Autowired
    private UserRepository userRepository;
	@Autowired
    private OrderService orderService;
	@Autowired
	private ExpenseService expenseService;
	@Autowired
	private AssignEmployeeService assignEmployeeService;
	
    @ModelAttribute
    private void userDetails(Model model, Principal principal){
        if(principal != null) {
        	String username = principal.getName();
            UserDtls user = userRepository.findByUsername(username);
            model.addAttribute("user",user);
            model.addAttribute("userRole",user.getRole());
        }else {
        	throw new UserNotAuthenticatedException("User is not authenticated");
        }
    }

	@GetMapping("/")
	public String home(Model model, Principal principal) {
		String username = principal.getName();
		UserDtls userDtls = userRepository.findByUsername(username);
		List<Order> todayOrdersByEmp = orderService.getOrdersByUserDtlsAndCurrentDate(userDtls);
		List<Order> userWiseOrders = orderService.findByUserDtls(userDtls);
		List<Expense> todayExpenses = expenseService.getExpensesByUserAndCurrentDateTimeRange(userDtls);
		List<Expense> totalExpenses = expenseService.getExpensesByUserDtls(userDtls);
		List<AssignEmployee> userAssignList = assignEmployeeService.getAssignsByUserDtls(userDtls);
		List<AssignEmployee> todayUserAssignList = assignEmployeeService.getAssignsByUserDtlsAndCurrentDateTimeRange(userDtls);
		
		int todayOrderAmount = todayOrdersByEmp.stream()
		        .mapToInt(Order::getOrderAmount)
		        .sum();
		int todayExpenseAmount = todayExpenses.stream()
				.mapToInt(Expense::getExpenseAmount)
				.sum();
		int totalExpenseAmount = totalExpenses.stream()
				.mapToInt(Expense::getExpenseAmount)
				.sum();
		int todayAssignAmount = todayUserAssignList.stream()
				.mapToInt(AssignEmployee::getAssignAmount)
				.sum();
		int totalAssignAmount = userAssignList.stream()
				.mapToInt(AssignEmployee::getAssignAmount)
				.sum();
		int userTotalOrderAmount = userWiseOrders.stream()
				.mapToInt(Order::getOrderAmount)
				.sum();
		int hasAmount = totalAssignAmount - userTotalOrderAmount - totalExpenseAmount;
        model.addAttribute("todayOrderAmount",todayOrderAmount);
        model.addAttribute("todayOrders",todayOrdersByEmp.size());
        model.addAttribute("todayAssignAmount",todayAssignAmount);
        model.addAttribute("totalAssignAmount",totalAssignAmount);
        model.addAttribute("todayExpenseAmount",todayExpenseAmount);
        model.addAttribute("totalExpenseAmount",totalExpenseAmount);
        model.addAttribute("hasAmount",hasAmount);
		return "employee/home";
	}
}

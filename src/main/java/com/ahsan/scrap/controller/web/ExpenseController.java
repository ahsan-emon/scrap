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

import com.ahsan.scrap.constraint.CommonConstraint;
import com.ahsan.scrap.model.Expense;
import com.ahsan.scrap.model.UserDtls;
import com.ahsan.scrap.repository.ExpenseRepository;
import com.ahsan.scrap.repository.UserRepository;
import com.ahsan.scrap.service.ExpenseService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/expense")
public class ExpenseController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ExpenseService expenseService;
	@Autowired
	private ExpenseRepository expenseRepository;
	
	@ModelAttribute
    private void userDetails(Model model, Principal principal){
        String username = principal.getName();
        UserDtls user = userRepository.findByUsername(username);
        model.addAttribute("userRole",user.getRole());
        model.addAttribute("user",user);
    }
	
	//expense of employee
	@GetMapping("/expenseList")
    public String expenseList(Model model, Principal principal) {
		String username = principal.getName();
        UserDtls userDtls = userRepository.findByUsername(username);
		if(userDtls != null) {
			if(userDtls.getRole().equals(CommonConstraint.ROLE_ADMIN)) {
				List<Expense> expenses = expenseService.getExpensesByExpenseDateDesc();
				model.addAttribute("expenses",expenses);
	        }else {
				List<Expense> expenses = expenseService.getExpensesByUserUptoPrevExpenseDate(userDtls);
	        	model.addAttribute("expenses", expenses);
	        }
		}
        return "expense/expense_list";
    }
	@GetMapping("/todayExpenseList")
	public String todayExpenseList(Model model, Principal principal) {
		String username = principal.getName();
		UserDtls userDtls = userRepository.findByUsername(username);
		if(userDtls.getRole().equals(CommonConstraint.ROLE_ADMIN)) {
			List<Expense> todayExpenses = expenseService.getExpensesWithinCurrentDateTimeRange();
			model.addAttribute("expenses",todayExpenses);
		}else {
			List<Expense> todayExpensesForUesr = expenseService.getExpensesByUserAndCurrentDateTimeRange(userDtls);
			model.addAttribute("expenses",todayExpensesForUesr);
		}
		return "expense/expense_list";
	}
	@GetMapping("/addExpense")
	public String expenseOfEmployee(HttpSession session, Model model) {
		String msg = (String) session.getAttribute("msg");
		if (msg != null) {
			model.addAttribute("msg", msg);
			session.removeAttribute("msg");
		}
		return "expense/add_expense";
	}
	@GetMapping("/expenseEdit/{id}")
	public String editExpense(@PathVariable("id") Long id, Model model) {
		Expense expense = expenseRepository.findById(id).orElse(null);
        model.addAttribute("expense", expense);
        return "expense/add_expense";
	}
	@PostMapping("/saveExpense")
    public String saveExpense(@ModelAttribute Expense expense, HttpSession session){
		String msg = null;
		Expense expenseObj = expenseService.saveExpense(expense);
        if(expenseObj != null) {
        	msg = "Expense add Successfully!";
        }else {
        	msg = "Something went wrong on the server!";
        }
        session.setAttribute("msg", msg);
        return "redirect:/expense/addExpense";
    }
	@GetMapping("/expenseDelete/{id}")
	public String deleteExpense(@PathVariable("id") Long id) {
		expenseRepository.deleteById(id);
        return "redirect:/expense/expenseList";
	}
}

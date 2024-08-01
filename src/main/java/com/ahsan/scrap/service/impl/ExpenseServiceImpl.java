package com.ahsan.scrap.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahsan.scrap.model.Expense;
import com.ahsan.scrap.model.UserDtls;
import com.ahsan.scrap.repository.ExpenseRepository;
import com.ahsan.scrap.repository.UserRepository;
import com.ahsan.scrap.service.ExpenseService;
import com.ahsan.scrap.util.UserUtil;

@Service
public class ExpenseServiceImpl implements ExpenseService{
	@Autowired
	private ExpenseRepository expenseRepository;
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Expense saveExpense(Expense expense) {
		String currentUsername = UserUtil.getCurrentUsername();
        UserDtls userDtls = userRepository.findByUsername(currentUsername);
		if(userDtls != null) {
			expense.setExpenseDate(LocalDateTime.now());
			expense.setUserDtls(userDtls);
			return expenseRepository.save(expense);
		}
		return null;
	}
	@Override
	public List<Expense> getExpensesByExpenseDateDesc(){
		return expenseRepository.findAllByOrderByExpenseDateDesc();
	}
	@Override
    public List<Expense> getExpensesByUserUptoPrevExpenseDate(UserDtls userDtls) {
    	LocalDate endDate = LocalDate.now();
    	LocalDate startDate = endDate.minusDays(1);
    	return expenseRepository.findByUserUptoPrevExpenseDate(userDtls,startDate, endDate);
    }
	@Override
    public List<Expense> getExpensesWithinCurrentDateTimeRange() {
    	// Get the current date
        LocalDate today = LocalDate.now();
        // Define the start of the period (6 AM today)
        LocalDateTime startDateTime = today.atTime(6, 0);
        // Define the end of the period (6 AM the next day)
        LocalDateTime endDateTime = today.plusDays(1).atTime(6, 0);
    	return expenseRepository.findExpensesWithinCurrentDateTimeRange(startDateTime, endDateTime);
    }
    @Override
    public List<Expense> getExpensesByUserAndCurrentDateTimeRange(UserDtls userDtls) {
    	// Get the current date
        LocalDate today = LocalDate.now();
        // Define the start of the period (6 AM today)
        LocalDateTime startDateTime = today.atTime(6, 0);
        // Define the end of the period (6 AM the next day)
        LocalDateTime endDateTime = today.plusDays(1).atTime(6, 0);
    	return expenseRepository.findExpensesByUserAndCurrentDateTimeRange(userDtls, startDateTime, endDateTime);
    }
    @Override
    public List<Expense> getExpensesByUserDtls(UserDtls userDtls){
		return expenseRepository.findByUserDtls(userDtls);
	}
}

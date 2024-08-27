package com.ahsan.scrap.service;

import java.time.LocalDate;
import java.util.List;

import com.ahsan.scrap.model.Expense;
import com.ahsan.scrap.model.UserDtls;

public interface ExpenseService {
	public Expense saveExpense(Expense expense);
	public List<Expense> getExpensesByExpenseDateDesc();
    public List<Expense> getExpensesByUserUptoPrevExpenseDate(UserDtls userDtls);
    public List<Expense> getExpensesWithinCurrentDateTimeRange();
    public List<Expense> getExpensesByUserAndCurrentDateTimeRange(UserDtls userDtls);
    public List<Expense> getExpensesByUserDtls(UserDtls userDtls);
    public List<Expense> searchExpenses(LocalDate fromDate, LocalDate toDate, Long userId);
}

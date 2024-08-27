package com.ahsan.scrap.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ahsan.scrap.model.Expense;
import com.ahsan.scrap.model.UserDtls;

public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense>  {
	List<Expense> findAllByOrderByExpenseDateDesc();
	List<Expense> findByUserDtls(UserDtls userDtls);
	@Query("SELECT o FROM Expense o WHERE o.userDtls = :userDtls AND DATE(o.expenseDate) BETWEEN :startOfDay AND :endOfDay")
    List<Expense> findByUserUptoPrevExpenseDate(@Param("userDtls") UserDtls userDtls, @Param("startOfDay") LocalDate startOfDay, @Param("endOfDay") LocalDate endOfDay);
	@Query("SELECT o FROM Expense o WHERE o.expenseDate BETWEEN :start AND :end")
    List<Expense> findExpensesWithinCurrentDateTimeRange(@Param("start") LocalDateTime startDateTime, @Param("end") LocalDateTime endDateTime);
	@Query("SELECT o FROM Expense o WHERE o.userDtls = :userDtls AND o.expenseDate BETWEEN :start AND :end")
    List<Expense> findExpensesByUserAndCurrentDateTimeRange(@Param("userDtls") UserDtls userDtls, @Param("start") LocalDateTime startDateTime, @Param("end") LocalDateTime endDateTime);
}

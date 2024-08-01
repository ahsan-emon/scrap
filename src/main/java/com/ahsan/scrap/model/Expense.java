package com.ahsan.scrap.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Setter
@Getter
@Table(name = "expense")
public class Expense {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "expense_reason")
	private String expenseReason;
    @Column(name = "expense_amount")
    private int expenseAmount;
    @Column(name = "expense_date")
    private LocalDateTime expenseDate;
    @ManyToOne
	@JoinColumn(name = "user_dtls_id")
    private UserDtls userDtls;
}

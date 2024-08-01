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
@Table(name = "assign_employee")
public class AssignEmployee {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "assign_amount")
    private int assignAmount;
    @Column(name = "assign_date")
    private LocalDateTime assignDate;
    @ManyToOne
	@JoinColumn(name = "user_dtls_id")
    private UserDtls userDtls;
    @ManyToOne
	@JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
}

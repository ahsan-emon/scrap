package com.ahsan.scrap.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Setter
@Getter
@Table(name = "vehicle")
public class Vehicle {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String model;
    @Column(name = "number_plate")
	private String numberPlate;
    @Column(name = "vehicle_exp_date")
    private LocalDate vehicleExpDate;
}

package com.ahsan.scrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahsan.scrap.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	public boolean existsByNumberPlate(String numberPlate);
}

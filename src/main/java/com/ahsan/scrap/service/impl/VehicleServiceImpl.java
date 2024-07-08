package com.ahsan.scrap.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahsan.scrap.repository.VehicleRepository;
import com.ahsan.scrap.service.VehicleService;

@Service
public class VehicleServiceImpl implements VehicleService{
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Override
	public boolean existsByNumberPlate(String numberPlate) {
		return vehicleRepository.existsByNumberPlate(numberPlate);
	}
}

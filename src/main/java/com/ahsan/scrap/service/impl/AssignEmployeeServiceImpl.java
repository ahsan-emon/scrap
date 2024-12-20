package com.ahsan.scrap.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ahsan.scrap.model.AssignEmployee;
import com.ahsan.scrap.model.UserDtls;
import com.ahsan.scrap.model.Vehicle;
import com.ahsan.scrap.repository.AssignEmployeeRepository;
import com.ahsan.scrap.repository.UserRepository;
import com.ahsan.scrap.repository.VehicleRepository;
import com.ahsan.scrap.service.AssignEmployeeService;

import jakarta.persistence.criteria.Predicate;

@Service
public class AssignEmployeeServiceImpl implements AssignEmployeeService{
	@Autowired
	private AssignEmployeeRepository assignEmployeeRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Override
	public AssignEmployee saveAssign(AssignEmployee assignEmployee, Long userDtlsId, Long vehicleId) {
		if(userDtlsId != null) {
			UserDtls userDtls = userRepository.findById(userDtlsId).orElse(null);
			if(userDtls != null) {
				assignEmployee.setAssignDate(LocalDateTime.now());
				assignEmployee.setUserDtls(userDtls);
				if(vehicleId != null) {
					Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);
					if(vehicle != null) {
						assignEmployee.setVehicle(vehicle);
					}
				}
				return assignEmployeeRepository.save(assignEmployee);
			}
		}
		return null;
	}
	@Override
	public List<AssignEmployee> getAssignEmployeesByAssignDateDesc(){
		return assignEmployeeRepository.findAllByOrderByAssignDateDesc();
	}
	@Override
    public List<AssignEmployee> getAssignsWithinCurrentDateTimeRange() {
    	// Get the current date
        LocalDate today = LocalDate.now();
        // Define the start of the period (6 AM today)
        LocalDateTime startDateTime = today.atTime(6, 0);
        // Define the end of the period (6 AM the next day)
        LocalDateTime endDateTime = today.plusDays(1).atTime(6, 0);
    	return assignEmployeeRepository.findAssignsWithinCurrentDateTimeRange(startDateTime, endDateTime);
    }
	@Override
	public List<AssignEmployee> getAssignsByUserDtls(UserDtls userDtls){
		return assignEmployeeRepository.findByUserDtls(userDtls);
	}
	@Override
    public List<AssignEmployee> getAssignsByUserDtlsAndCurrentDateTimeRange(UserDtls userDtls) {
    	// Get the current date
        LocalDate today = LocalDate.now();
        // Define the start of the period (6 AM today)
        LocalDateTime startDateTime = today.atTime(6, 0);
        // Define the end of the period (6 AM the next day)
        LocalDateTime endDateTime = today.plusDays(1).atTime(6, 0);
    	return assignEmployeeRepository.findAssignsByUserAndCurrentDateTimeRange(userDtls, startDateTime, endDateTime);
    }
	@Override
	public List<AssignEmployee> searchAssignEmployees(LocalDate fromDate, LocalDate toDate, Long userId) {
	    Specification<AssignEmployee> spec = (root, query, criteriaBuilder) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        if (fromDate != null) {
	            // Extract only the date part from assignDate and compare
	            predicates.add(criteriaBuilder.greaterThanOrEqualTo(
	                criteriaBuilder.function("DATE", LocalDate.class, root.get("assignDate")), fromDate));
	        }

	        if (toDate != null) {
	            // Extract only the date part from assignDate and compare
	            predicates.add(criteriaBuilder.lessThanOrEqualTo(
	                criteriaBuilder.function("DATE", LocalDate.class, root.get("assignDate")), toDate));
	        }

	        if (userId != null) {
	            predicates.add(criteriaBuilder.equal(root.get("userDtls").get("id"), userId));
	        }

	        query.orderBy(criteriaBuilder.desc(root.get("assignDate")));

	        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	    };

	    return assignEmployeeRepository.findAll(spec);
	}
}

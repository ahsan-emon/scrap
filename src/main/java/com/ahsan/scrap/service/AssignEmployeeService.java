package com.ahsan.scrap.service;

import java.time.LocalDate;
import java.util.List;

import com.ahsan.scrap.model.AssignEmployee;
import com.ahsan.scrap.model.UserDtls;

public interface AssignEmployeeService {
	public AssignEmployee saveAssign(AssignEmployee assignEmployee, Long userDtlsId, Long vehicleId);
	public List<AssignEmployee> getAssignEmployeesByAssignDateDesc();
	public List<AssignEmployee> getAssignsWithinCurrentDateTimeRange();
	public List<AssignEmployee> getAssignsByUserDtls(UserDtls userDtls);
	public List<AssignEmployee> getAssignsByUserDtlsAndCurrentDateTimeRange(UserDtls userDtls);
	public List<AssignEmployee> searchAssignEmployees(LocalDate fromDate, LocalDate toDate, Long userId);
}

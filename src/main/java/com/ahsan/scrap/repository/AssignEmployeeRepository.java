package com.ahsan.scrap.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ahsan.scrap.model.AssignEmployee;
import com.ahsan.scrap.model.UserDtls;

public interface AssignEmployeeRepository extends JpaRepository<AssignEmployee, Long> {
	List<AssignEmployee> findAllByOrderByAssignDateDesc();
	@Query("SELECT o FROM AssignEmployee o WHERE o.assignDate BETWEEN :start AND :end")
    List<AssignEmployee> findAssignsWithinCurrentDateTimeRange(@Param("start") LocalDateTime startDateTime, @Param("end") LocalDateTime endDateTime);
	List<AssignEmployee> findByUserDtls(UserDtls userDtls);
	@Query("SELECT o FROM AssignEmployee o WHERE o.userDtls = :userDtls AND o.assignDate BETWEEN :start AND :end")
    List<AssignEmployee> findAssignsByUserAndCurrentDateTimeRange(@Param("userDtls") UserDtls userDtls, @Param("start") LocalDateTime startDateTime, @Param("end") LocalDateTime endDateTime);
}

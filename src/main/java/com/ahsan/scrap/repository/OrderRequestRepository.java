package com.ahsan.scrap.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ahsan.scrap.model.OrderRequest;

public interface OrderRequestRepository extends JpaRepository<OrderRequest, Long> {
	List<OrderRequest> findAllByOrderByRequestDateDesc();
	@Query("SELECT o FROM OrderRequest o WHERE DATE(o.requestDate) = :requestDate ORDER BY o.requestDate DESC")
    List<OrderRequest> findByRequestDateDesc(@Param("requestDate") LocalDate requestDate);
}

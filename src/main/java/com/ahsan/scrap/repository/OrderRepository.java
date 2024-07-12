package com.ahsan.scrap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahsan.scrap.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
	List<Order> findAllByOrderByOrderDateDesc();
}

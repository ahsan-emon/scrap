package com.ahsan.scrap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahsan.scrap.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	public boolean existsByUsername(String username);
    public Customer findByUsername(String username);
}

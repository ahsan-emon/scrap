package com.ahsan.scrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahsan.scrap.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	public boolean existsByShortName(String shortName);
}

package com.ahsan.scrap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahsan.scrap.model.Product;
import com.ahsan.scrap.model.SellItem;

public interface SellItemRepository extends JpaRepository<SellItem, Long>{
	List<SellItem> findByProduct(Product product);
}

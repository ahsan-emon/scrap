package com.ahsan.scrap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahsan.scrap.model.Order;
import com.ahsan.scrap.model.OrderItem;
import com.ahsan.scrap.model.Product;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	List<OrderItem> findByProduct(Product product);
	List<OrderItem> findByOrder(Order order);
}

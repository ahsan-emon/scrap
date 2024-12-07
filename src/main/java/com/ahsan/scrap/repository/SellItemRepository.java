package com.ahsan.scrap.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ahsan.scrap.model.Product;
import com.ahsan.scrap.model.SellItem;

public interface SellItemRepository extends JpaRepository<SellItem, Long>{
	List<SellItem> findByProduct(Product product);
	@Query("SELECT o FROM SellItem o WHERE " +
		       "(:fromDate IS NULL OR DATE(o.sell.sellDate) >= :fromDate) AND " +
		       "(:toDate IS NULL OR DATE(o.sell.sellDate) <= :toDate) AND " +
		       "(:productId IS NULL OR o.product.id = :productId) " +
		       "ORDER BY o.sell.sellDate DESC")
	List<SellItem> findAllBySellDateDesc(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, @Param("productId") Long productId);
}



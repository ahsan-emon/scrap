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
	@Query("SELECT o FROM SellItem o WHERE (:sellDate IS NULL OR DATE(o.sell.sellDate) = :sellDate) ORDER BY o.sell.sellDate DESC")
	List<SellItem> findAllBySellDateDesc(@Param("sellDate") LocalDate sellDate);
}

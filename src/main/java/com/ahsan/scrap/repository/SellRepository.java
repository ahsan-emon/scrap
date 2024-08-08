package com.ahsan.scrap.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ahsan.scrap.model.Sell;
import com.ahsan.scrap.model.UserDtls;

public interface SellRepository extends JpaRepository<Sell, Long>{
	@Query("SELECT o FROM Sell o WHERE DATE(o.sellDate) = :sellDate")
    List<Sell> findBySellDate(@Param("sellDate") LocalDate sellDate);
	@Query("SELECT o FROM Sell o WHERE o.userDtls = :userDtls AND DATE(o.sellDate) BETWEEN :startOfDay AND :endOfDay ORDER BY o.sellDate DESC")
    List<Sell> findByUserDtlsAndUptoPrevSellDateDesc(@Param("userDtls") UserDtls userDtls, @Param("startOfDay") LocalDate startOfDay, @Param("endOfDay") LocalDate endOfDay);
    @Query("SELECT o FROM Sell o WHERE o.userDtls = :userDtls AND DATE(o.sellDate) = :today")
    List<Sell> findByUserDtlsAndCurrentDate(@Param("userDtls") UserDtls userDtls, @Param("today") LocalDate today);
    
   @Query("SELECT o FROM Sell o WHERE " +
    	       "(:fromDate IS NULL OR DATE(o.sellDate) >= :fromDate) AND " +
    	       "(:toDate IS NULL OR DATE(o.sellDate) <= :toDate) AND " +
    	       "(:userId IS NULL OR o.userDtls.id = :userId) " +
    	       "ORDER BY o.sellDate DESC")
    List<Sell> searchSells(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, @Param("userId") Long userId);

}

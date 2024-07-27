package com.ahsan.scrap.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ahsan.scrap.model.Customer;
import com.ahsan.scrap.model.Order;
import com.ahsan.scrap.model.UserDtls;


public interface OrderRepository extends JpaRepository<Order, Long>{
	List<Order> findAllByOrderByOrderDateDesc();
	List<Order> findByUserDtls(UserDtls userDtls);
	List<Order> findByCustomer(Customer customer);
	@Query("SELECT o FROM Order o WHERE DATE(o.orderDate) = :orderDate")
    List<Order> findByOrderDate(@Param("orderDate") LocalDate orderDate);
    @Query("SELECT o FROM Order o WHERE o.userDtls = :userDtls AND DATE(o.orderDate) BETWEEN :startOfDay AND :endOfDay")
    List<Order> findByUserDtlsAndUptoPrevOrderDate(@Param("userDtls") UserDtls userDtls, @Param("startOfDay") LocalDate startOfDay, @Param("endOfDay") LocalDate endOfDay);
    @Query("SELECT o FROM Order o WHERE o.userDtls = :userDtls AND DATE(o.orderDate) = :orderDate")
    List<Order> findByUserDtlsAndOrderDate(@Param("userDtls") UserDtls userDtls, @Param("orderDate") LocalDate orderDate);
}

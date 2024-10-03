package com.ahsan.scrap.service;

import java.time.LocalDate;
import java.util.List;

import com.ahsan.scrap.model.Customer;
import com.ahsan.scrap.model.Order;
import com.ahsan.scrap.model.Product;
import com.ahsan.scrap.model.UserDtls;

public interface OrderService {
    public Order saveOrder(Order order, Long customerId);
    public List<Customer> getAllCustomers();
    public List<Product> getAllProducts();
    public List<Order> getOrders();
    public List<Order> getOrdersByOrderDateDesc();
    public Order updateOrder(Order order);
    public List<Order> findByUserDtls(UserDtls userDtls);
    public List<Order> getOrdersByCurrentDate();
    public List<Order> getOrdersWithinDateTimeRange();
    public List<Order> getOrdersByUserDtlsAndUptoPrevOrderDate(UserDtls userDtls);
    public List<Order> getOrdersByUserDtlsAndCurrentDate(UserDtls userDtls);
    public List<Order> getOrdersByUserDtlsAndCurrentDateTimeRange(UserDtls userDtls);
    public List<Order> searchOrders(LocalDate fromDate, LocalDate toDate, Long customerId, Long userId);
    public List<Order> searchOrdersWithDateForCalculation(LocalDate fromDate, LocalDate toDate);
}

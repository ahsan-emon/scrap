package com.ahsan.scrap.service;

import java.util.List;

import com.ahsan.scrap.model.Customer;
import com.ahsan.scrap.model.Order;
import com.ahsan.scrap.model.Product;

public interface OrderService {
    public Order saveOrder(Order order, Long customerId);
    public List<Customer> getAllCustomers();
    public List<Product> getAllProducts();
    public List<Order> getOrders();
    public List<Order> getOrdersByOrderDateDesc();
}

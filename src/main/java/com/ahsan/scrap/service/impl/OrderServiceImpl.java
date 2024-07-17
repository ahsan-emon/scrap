package com.ahsan.scrap.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahsan.scrap.model.Customer;
import com.ahsan.scrap.model.Order;
import com.ahsan.scrap.model.OrderItem;
import com.ahsan.scrap.model.Product;
import com.ahsan.scrap.model.UserDtls;
import com.ahsan.scrap.repository.CustomerRepository;
import com.ahsan.scrap.repository.OrderRepository;
import com.ahsan.scrap.repository.ProductRepository;
import com.ahsan.scrap.repository.UserRepository;
import com.ahsan.scrap.service.OrderService;
import com.ahsan.scrap.util.UserUtil;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    @Override
    public Order saveOrder(Order order, Long customerId) {
    	int totalAmount = 0;
    	int numberOfItems = 0;
    	// Calculate total order amount
        order.getOrderItems().forEach(item -> item.calculateAmount());
        
        // Save the order first to get the ID
        Order savedOrder = orderRepository.save(order);
        
        // Link orderItems to order
        if (savedOrder.getOrderItems() != null) {
        	numberOfItems = savedOrder.getOrderItems().size();
            for (OrderItem item : savedOrder.getOrderItems()) {
                item.setOrder(savedOrder);
                totalAmount = item.getAmount() + totalAmount;
            }
        }
        order.setOrderDate(LocalDateTime.now());
        String currentUsername = UserUtil.getCurrentUsername();
        UserDtls userDtls = userRepository.findByUsername(currentUsername);
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(userDtls != null && customer != null) {
            order.setUserDtls(userDtls);
            order.setCustomer(customer);
        }
        order.setNumberOfItems(numberOfItems);
        order.setOrderAmount(totalAmount);
        return orderRepository.save(order);
    }
    
    @Override
    public Order updateOrder(Order order) {
    	int totalAmount = 0;
    	int numberOfItems = 0;
    	// Calculate total order amount
        Order currentOrder = orderRepository.findById(order.getId()).orElse(null);
        if(currentOrder != null) {
            order.getOrderItems().forEach(item -> item.calculateAmount());
        	order.setOrderDate(currentOrder.getOrderDate());
        	order.setCustomer(currentOrder.getCustomer());
        	order.setUserDtls(currentOrder.getUserDtls());
        	// Link orderItems to order
            if (order.getOrderItems() != null) {
            	numberOfItems = order.getOrderItems().size();
                for (OrderItem item : order.getOrderItems()) {
                    item.setOrder(order);
                    totalAmount = item.getAmount() + totalAmount;
                }
            }
            order.setNumberOfItems(numberOfItems);
            order.setOrderAmount(totalAmount);
            return orderRepository.save(order);
        }
        return null;
    }
    
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }
    @Override
    public List<Order> getOrdersByOrderDateDesc() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }
    @Override
    public 	List<Order> findByUserDtls(UserDtls userDtls){
    	return orderRepository.findByUserDtls(userDtls);
    }
    @Override
    public List<Order> getOrdersByCurrentDate() {
        LocalDate today = LocalDate.now();
        return orderRepository.findByOrderDate(today);
    }
    @Override
    public List<Order> getOrdersByUserDtlsAndCurrentDate(UserDtls userDtls) {
        LocalDate today = LocalDate.now();
        return orderRepository.findByUserDtlsAndOrderDate(userDtls, today);
    }
    @Override
    public List<Order> getOrdersByUserDtlsAndUptoPrevOrderDate(UserDtls userDtls) {
    	LocalDate endDate = LocalDate.now();
    	LocalDate startDate = endDate.minusDays(1);
    	return orderRepository.findByUserDtlsAndUptoPrevOrderDate(userDtls,startDate, endDate);
    }
}

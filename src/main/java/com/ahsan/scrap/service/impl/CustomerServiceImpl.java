package com.ahsan.scrap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ahsan.scrap.model.Customer;
import com.ahsan.scrap.repository.CustomerRepository;
import com.ahsan.scrap.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Customer createCustomer(Customer cust){
    	cust.setPassword(passwordEncoder.encode(cust.getPassword()));
    	cust.setRole("ROLE_CUSTOMER");
        return customerRepository.save(cust);
    }
    @Override
    public boolean checkUsername(String username){
        return customerRepository.existsByUsername(username);
    }
    @Override
    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }
    @Override
    public Customer updateCustomer(Customer customer) {
    	return customerRepository.save(customer);
    }

}

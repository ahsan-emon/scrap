package com.ahsan.scrap.service;

import java.util.List;

import com.ahsan.scrap.model.Customer;

public interface CustomerService {
	public Customer createCustomer(Customer cust);
    public boolean checkUsername(String username);
    public List<Customer> getCustomers();
}

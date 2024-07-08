package com.ahsan.scrap.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahsan.scrap.repository.ProductRepository;
import com.ahsan.scrap.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
	@Autowired
	private ProductRepository productRepository;
	
	@Override
    public boolean checkShortName(String shortName) {
        return productRepository.existsByShortName(shortName);
	}
}

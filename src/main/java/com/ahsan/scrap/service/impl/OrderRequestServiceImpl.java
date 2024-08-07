package com.ahsan.scrap.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahsan.scrap.model.OrderRequest;
import com.ahsan.scrap.repository.OrderRequestRepository;
import com.ahsan.scrap.service.OrderRequestService;

@Service
public class OrderRequestServiceImpl implements OrderRequestService{
	@Autowired
	public OrderRequestRepository orderRequestRepository;
	
	@Override
	public OrderRequest saveOrderRequest(OrderRequest orderRequest) {
		orderRequest.setRequestDate(LocalDateTime.now());
		return orderRequestRepository.save(orderRequest);
	}
	@Override
	public List<OrderRequest> getOrderRequestsByCurrentDateDesc(){
		LocalDate today = LocalDate.now();
        return orderRequestRepository.findByRequestDateDesc(today);
	}
}

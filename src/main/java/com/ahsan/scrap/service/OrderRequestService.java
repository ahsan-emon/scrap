package com.ahsan.scrap.service;

import java.util.List;

import com.ahsan.scrap.model.OrderRequest;

public interface OrderRequestService {
	public OrderRequest saveOrderRequest(OrderRequest orderRequest);
    public List<OrderRequest> getOrderRequestsByCurrentDateDesc();
}

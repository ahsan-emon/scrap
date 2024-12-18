package com.ahsan.scrap.util;

public class CustomerOrderSummary {
	private int totalOrders;
    private double totalOrderAmount;
    private double totalOrderQuantity;

    public CustomerOrderSummary(int totalOrders, double totalOrderAmount, double totalOrderQuantity) {
        this.totalOrders = totalOrders;
        this.totalOrderAmount = totalOrderAmount;
        this.totalOrderQuantity = totalOrderQuantity;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public double getTotalOrderAmount() {
        return totalOrderAmount;
    }
    public double getTotalOrderQuantity() {
    	return totalOrderQuantity;
    }
}

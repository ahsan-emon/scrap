package com.ahsan.scrap.service;

import java.time.LocalDate;
import java.util.List;

import com.ahsan.scrap.model.Product;
import com.ahsan.scrap.model.Sell;
import com.ahsan.scrap.model.UserDtls;

public interface SellService {
	public Sell saveSell(Sell sell);
    public List<Product> getAllProducts();
    public List<Sell> getSellsByCurrentDate();
    public List<Sell> getSellsByUserDtlsAndUptoPrevSellDateDesc(UserDtls userDtls);
    public List<Sell> getSellsByUserDtlsAndCurrentDate(UserDtls userDtls);
    public List<Sell> searchSells(LocalDate fromDate, LocalDate toDate, Long userId);
}

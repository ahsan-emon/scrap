package com.ahsan.scrap.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahsan.scrap.model.Product;
import com.ahsan.scrap.model.Sell;
import com.ahsan.scrap.model.SellItem;
import com.ahsan.scrap.model.UserDtls;
import com.ahsan.scrap.repository.ProductRepository;
import com.ahsan.scrap.repository.SellRepository;
import com.ahsan.scrap.repository.UserRepository;
import com.ahsan.scrap.service.SellService;
import com.ahsan.scrap.util.UserUtil;

import jakarta.transaction.Transactional;

@Service
public class SellServiceImpl implements SellService {
	@Autowired
    private SellRepository sellRepository;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Transactional
	@Override
	public Sell saveSell(Sell sell) {
    	Float totalAmount = 0f;
    	Float totalQuantity = 0f;
    	int numberOfItems = 0;
    	// Calculate total sell amount
    	sell.getSellItems().forEach(item -> item.calculateAmount());
    	if(sell.getId() == null) {
        	sell.setSellDate(LocalDateTime.now());
        }else {
        	Sell oldSell = sellRepository.findById(sell.getId()).orElse(null);
        	sell.setSellDate(oldSell.getSellDate());
        }
        // Save the sell first to get the ID
        Sell savedSell = sellRepository.save(sell);
        
        // Link sellItems to sell
        if (savedSell.getSellItems() != null) {
        	numberOfItems = savedSell.getSellItems().size();
            for (SellItem item : savedSell.getSellItems()) {
                item.setSell(savedSell);
                totalAmount = item.getAmount() + totalAmount;
                totalQuantity = item.getQuantity() + totalQuantity;
            }
        }
        totalQuantity = Float.parseFloat(String.format("%.2f", totalQuantity));
        totalAmount = Float.parseFloat(String.format("%.2f", totalAmount));
        String currentUsername = UserUtil.getCurrentUsername();
        UserDtls userDtls = userRepository.findByUsername(currentUsername);
        if(userDtls != null) {
            sell.setUserDtls(userDtls);
        }
        sell.setNumberOfItems(numberOfItems);
        sell.setSellAmount(totalAmount);
        sell.setSellQuantity(totalQuantity);
        return sellRepository.save(sell);
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public List<Sell> getSellsByCurrentDate() {
		LocalDate today = LocalDate.now();
        return sellRepository.findBySellDate(today);
	}

	@Override
	public List<Sell> getSellsByUserDtlsAndUptoPrevSellDateDesc(UserDtls userDtls) {
		LocalDate endDate = LocalDate.now();
    	LocalDate startDate = endDate.minusDays(1);
    	return sellRepository.findByUserDtlsAndUptoPrevSellDateDesc(userDtls,startDate, endDate);
	}

	@Override
	public List<Sell> getSellsByUserDtlsAndCurrentDate(UserDtls userDtls) {
		LocalDate today = LocalDate.now();
        return sellRepository.findByUserDtlsAndCurrentDate(userDtls, today);
	}

	@Override
	public List<Sell> searchSells(LocalDate fromDate, LocalDate toDate, Long userId) {
		return sellRepository.searchSells(fromDate, toDate, userId);
	}

}

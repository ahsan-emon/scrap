package com.ahsan.scrap.controller.web;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ahsan.scrap.constraint.CommonConstraint;
import com.ahsan.scrap.model.Product;
import com.ahsan.scrap.model.Sell;
import com.ahsan.scrap.model.SellItem;
import com.ahsan.scrap.model.UserDtls;
import com.ahsan.scrap.repository.ProductRepository;
import com.ahsan.scrap.repository.SellRepository;
import com.ahsan.scrap.repository.UserRepository;
import com.ahsan.scrap.service.SellService;
import com.ahsan.scrap.util.FileUploadService;

@Controller
@RequestMapping("/sell")
public class SellController {
	@Autowired
    private SellService sellService;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private SellRepository sellRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FileUploadService fileUploadService;
	
	@ModelAttribute
    private void userDetails(Model model, Principal principal){
        String username = principal.getName();
        UserDtls user = userRepository.findByUsername(username);
        model.addAttribute("userRole",user.getRole());
        model.addAttribute("user",user);
    }
	
	//sell 
		
		//sell list with search mechanism
		@GetMapping("/sell_list")
		public String sellList(
		    @RequestParam(name = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
		    @RequestParam(name = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
		    @RequestParam(name = "userId", required = false) Long userId,
		    Model model,
		    Principal principal) {
	
		    String username = principal.getName();
		    UserDtls userDtls = userRepository.findByUsername(username);
		    
		    List<Sell> sells;
		    if (userDtls.getRole().equals(CommonConstraint.ROLE_ADMIN)) {
		        sells = sellService.searchSells(fromDate, toDate, userId);
		    } else {
		        sells = sellService.getSellsByUserDtlsAndUptoPrevSellDateDesc(userDtls);
		    }
	
		    model.addAttribute("sells", sells);
	
		    if (userDtls.getRole().equals(CommonConstraint.ROLE_ADMIN)) {
		        List<UserDtls> userList = userRepository.findAll();
		        model.addAttribute("fromDate", fromDate);
			    model.addAttribute("toDate", toDate);
		        model.addAttribute("userId", userId);
		        model.addAttribute("userList", userList);
		    }
	
		    return "sell/sell_list";
		}

		
		@GetMapping("/today_sell_list")
		public String todaySellList(Model model, Principal principal) {
			String username = principal.getName();
			UserDtls userDtls = userRepository.findByUsername(username);
			if(userDtls.getRole().equals(CommonConstraint.ROLE_ADMIN)) {
				List<Sell> todaySells = sellService.getSellsByCurrentDate();
				model.addAttribute("sells",todaySells);
			}else {
				List<Sell> sellsForUser = sellService.getSellsByUserDtlsAndCurrentDate(userDtls);
				model.addAttribute("sells",sellsForUser);
			}
			return "sell/sell_list";
		}
		
		@GetMapping("/sellForm")
	    public String showsellForm(Model model, Principal principal) {
	        Sell sell = new Sell();
	        sell.setSellItems(new ArrayList<>(Collections.singletonList(new SellItem()))); // Initialize with one item

	        model.addAttribute("sell", sell);
	        model.addAttribute("products", sellService.getAllProducts());

	        return "sell/sellForm";
	    }

	    @GetMapping("/sellView/{id}")
	   	public String viewSell(@PathVariable("id") Long id, Model model) {
	   		Sell sell = sellRepository.findById(id).orElse(null);
	        model.addAttribute("sell", sell);
	        return "sell/view_sell";
	   	}
	    @GetMapping("/sellEdit/{id}")
		public String editSell(@PathVariable("id") Long id, Model model) {
			Sell sell = sellRepository.findById(id).orElse(null);
			List<Product> products = productRepository.findAll();
	        model.addAttribute("sell", sell);
	        model.addAttribute("products", products);
	        return "sell/edit_sell";
		}
	    @PostMapping("/saveSell")
	    public String saveSell(@ModelAttribute Sell sell, @RequestParam("photo") MultipartFile photo, Model model) {
	    	//file upload
			try {
				String previousFileName = "";
				if(sell.getId() != null) {
					Sell oldSell = sellRepository.findById(sell.getId()).orElse(null);
					if(oldSell != null && oldSell.getSellReceipt() != null) {
						previousFileName = oldSell.getSellReceipt();
					}
				}
				
				String fileName = fileUploadService.saveSellFile(photo, previousFileName);
				if (fileName != null) {
					sell.setSellReceipt(fileName);
		        }
				sellService.saveSell(sell);
		        return "redirect:/sell/sell_list";
			} catch (IOException e) {
				model.addAttribute("error", "Error uploading file: " + e.getMessage());
	            return "redirect:/sell/sellForm";
			}
	        
	    }
		@GetMapping("/sellDelete/{id}")
		public String deleteSell(@PathVariable("id") Long id) {
			sellRepository.deleteById(id);
	        return "redirect:/sell/sell_list";
		}
}


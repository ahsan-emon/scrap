package com.ahsan.scrap.controller.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ahsan.scrap.model.UserDtls;
import com.ahsan.scrap.repository.UserRepository;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
    private UserRepository userRepository;

    @ModelAttribute
    private void userDetails(Model model, Principal principal){
        String username = principal.getName();
        UserDtls user = userRepository.findByUsername(username);
        model.addAttribute("user",user);
    }

	@GetMapping("/")
	public String home() {
		return "customer/home";
	}
}

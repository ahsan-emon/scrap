package com.ahsan.scrap.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ahsan.scrap.model.OrderRequest;
import com.ahsan.scrap.model.Product;
import com.ahsan.scrap.model.UserDtls;
import com.ahsan.scrap.repository.ProductRepository;
import com.ahsan.scrap.service.OrderRequestService;
import com.ahsan.scrap.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class WebController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRequestService orderRequestService;

    @GetMapping("/")
    public String index(Model model){
    	List<Product> productList = productRepository.findAll();
    	model.addAttribute("productList", productList);
        return "index";
    }
    @GetMapping("/signin")
    public String login(){
        return "login";
    }
    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }
    @GetMapping("/register")
    public String register(HttpSession session, Model model) {
        String msg = (String) session.getAttribute("msg");
        if (msg != null) {
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }
        return "register";
    }
    @PostMapping("/createUser")
    public String createUser(@ModelAttribute UserDtls user, HttpSession session){
        String msg = null;
        if (!userService.checkUsername(user.getUsername())) {
            UserDtls userDetails = userService.createUser(user);
            if (userDetails != null) {
                msg = "Register Successfully!";
            } else {
                msg = "Something went wrong on the server!";
            }
        } else {
            msg = "Username already exists!";
        }
        session.setAttribute("msg", msg);
        return "redirect:/register";
    }
    @GetMapping("/orderRequest")
    public String orderRequest(HttpSession session, Model model) {
        String msg = (String) session.getAttribute("msg");
        if (msg != null) {
            model.addAttribute("msg", msg);
            session.removeAttribute("msg");
        }
        return "orderRequest";
    }
    @PostMapping("/saveOrderRequest")
    public String saveOrderRequest(@ModelAttribute OrderRequest orderRequest, HttpSession session) {
    	String msg = null;
    	OrderRequest orderRequestDtls = orderRequestService.saveOrderRequest(orderRequest);
        if (orderRequestDtls != null) {
            msg = "Request sent Successfully!";
        } else {
            msg = "Something went wrong on the server, please try again!";
        }
        session.setAttribute("msg", msg);
        return "redirect:/orderRequest";
    }
}

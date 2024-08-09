package com.ahsan.scrap.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahsan.scrap.model.UserDtls;
import com.ahsan.scrap.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
public class RestApiController {
	@Autowired
    private UserService userService;
	
	@PostMapping("/saveUser")
    public String saveUser(@ModelAttribute UserDtls user, HttpSession session){
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
        return msg;
    }
}

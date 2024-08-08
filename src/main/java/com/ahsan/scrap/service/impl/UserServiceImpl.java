package com.ahsan.scrap.service.impl;

import com.ahsan.scrap.model.UserDtls;
import com.ahsan.scrap.repository.UserRepository;
import com.ahsan.scrap.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDtls createUser(UserDtls user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        return userRepository.save(user);
    }
    @Override
    public UserDtls createUserByAdmin(UserDtls user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    @Override
    public boolean checkUsername(String username){
        return userRepository.existsByUsername(username);
    }
    @Override
    public List<UserDtls> getUserDtls(){
    	return userRepository.findAll();
    }
    @Override
    public UserDtls findUserById(Long id) {
    	return userRepository.findById(id).orElse(null);
    }
    @Override
    public UserDtls updateUserDtls(UserDtls user) {
    	UserDtls oldUserDtls = userRepository.findById(user.getId()).orElse(null);
    	if(oldUserDtls != null) {
    		if(!user.getPassword().isEmpty() && user.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
        	}else {
                user.setPassword(oldUserDtls.getPassword());
        	}
    		if(user.getPhotoPath() == null) {
    			user.setPhotoPath(oldUserDtls.getPhotoPath());
    		}
    		//TODO for other not update field
    		user.setUsername(oldUserDtls.getUsername());
        	return userRepository.save(user);
    	}
    	return null;
    }
}

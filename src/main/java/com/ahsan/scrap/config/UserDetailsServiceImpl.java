package com.ahsan.scrap.config;

import com.ahsan.scrap.model.UserDtls;
import com.ahsan.scrap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDtls user = userRepository.findByUsername(username);
        if(user != null){
            return new CustomUserDetails(user);
        }
        throw new UsernameNotFoundException("User not available!");
    }
}

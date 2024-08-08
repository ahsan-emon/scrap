package com.ahsan.scrap.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.ahsan.scrap.constraint.CommonConstraint;
import com.ahsan.scrap.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	public AuthenticationSuccessHandler customSuccessHandler;

    @Bean
    public UserDetailsService getUserDetailsService(){
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider getDaoAuthProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getUserDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                                .requestMatchers("/admin/**").hasAuthority(CommonConstraint.ROLE_ADMIN)
                                .requestMatchers("/order/**").hasAnyAuthority(CommonConstraint.ROLE_ADMIN,CommonConstraint.ROLE_EMPLOYEE)
                                .requestMatchers("/sell/**").hasAnyAuthority(CommonConstraint.ROLE_ADMIN,CommonConstraint.ROLE_EMPLOYEE)
                                .requestMatchers("/expense/**").hasAnyAuthority(CommonConstraint.ROLE_ADMIN,CommonConstraint.ROLE_EMPLOYEE)
                                .requestMatchers("/user/**").hasAnyAuthority(CommonConstraint.ROLE_ADMIN,CommonConstraint.ROLE_EMPLOYEE,CommonConstraint.ROLE_USER)
                                .requestMatchers("/customer/**").hasAuthority(CommonConstraint.ROLE_CUSTOMER)
                                .requestMatchers("/**").permitAll()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/signin")
                                .loginProcessingUrl("/login")
                                .successHandler(customSuccessHandler)
                )
                .logout(logout ->
                        logout
                                .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

//    @Bean
//    public void configure(AuthenticationManagerBuilder auth) throws Exception{
//        auth.authenticationProvider(getDaoAuthProvider());
//    }
}

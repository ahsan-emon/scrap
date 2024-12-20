package com.ahsan.scrap.config;

import java.io.IOException;
import java.util.Set;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.ahsan.scrap.constraint.CommonConstraint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class CustomSuccessHandler implements AuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		if(roles.contains(CommonConstraint.ROLE_ADMIN) || roles.contains(CommonConstraint.ROLE_OWNER)) {
			response.sendRedirect("/admin/");
		}else if(roles.contains(CommonConstraint.ROLE_CUSTOMER)) {
			response.sendRedirect("/customer/");
		}else if(roles.contains(CommonConstraint.ROLE_EMPLOYEE)) {
			response.sendRedirect("/employee/");
		}else {
			response.sendRedirect("/user/");
		}
	}

}

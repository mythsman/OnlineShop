package com.darglk.onlineshop.helpers;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.darglk.onlineshop.service.CartService;

@Component
public class CustomLogoutHandler implements LogoutHandler {
 
	@Autowired
	private CartService cartService;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		
		Long cartId = (Long)request.getSession().getAttribute("cart_id");
		if(cartId != null) {
			cartService.destroyCart(cartId);
		}
		try {
			request.logout();
			authentication.setAuthenticated(false);
			SecurityContextHolder.getContext().setAuthentication(null);
		} catch(ServletException e) {
			System.out.println("Could not log out.");
		}
	}
}

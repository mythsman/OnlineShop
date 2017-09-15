package com.darglk.onlineshop.helpers;

import javax.servlet.http.HttpSessionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.stereotype.Component;

import com.darglk.onlineshop.service.CartService;

@Component
public class SessionTimeoutHandler extends HttpSessionEventPublisher {
	
	@Autowired
	private CartService cartService;

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
	    
	    Long cartId = (Long)event.getSession().getAttribute("cart_id");
	    if(cartId != null) {
	    	cartService.destroyCart(cartId);
	    }
	    super.sessionDestroyed(event);
	}
}

package com.darglk.onlineshop.controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.darglk.onlineshop.helpers.FlashMessage;
import com.darglk.onlineshop.model.Cart;
import com.darglk.onlineshop.model.Order;
import com.darglk.onlineshop.model.User;
import com.darglk.onlineshop.service.CartService;
import com.darglk.onlineshop.service.OrderService;
import com.darglk.onlineshop.service.UserService;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/checkout", method=RequestMethod.POST)
	public String checkout(Model model, HttpServletRequest httpRequest, @RequestParam("shipping") String shippingCost) {
		BigDecimal shipping = new BigDecimal(shippingCost);
		Cart cart = cartService.findCart((Long)httpRequest.getSession().getAttribute("cart_id"));
		User user = userService.findByUsername(SecurityContextHolder.getContext()
                .getAuthentication().getName());
		Order order = orderService.placeOrder(cart, shipping, user);
		FlashMessage.createFlashMessage("info", "Your order has been placed successfully.", model);
		model.addAttribute("order", order);
		return "order_summary";
	}
}

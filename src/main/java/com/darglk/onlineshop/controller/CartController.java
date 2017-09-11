package com.darglk.onlineshop.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.darglk.onlineshop.model.Cart;
import com.darglk.onlineshop.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@RequestMapping(value="show", method=RequestMethod.GET)
	public String showCart(Model model, HttpServletRequest httpRequest) {
		Cart cart = cartService.findCart((Long)httpRequest.getSession().getAttribute("cart_id"));
		System.out.println("SHOPPING CART: " + cart);
		model.addAttribute("cart", cart);
		return "shoppingCart";
	}
}

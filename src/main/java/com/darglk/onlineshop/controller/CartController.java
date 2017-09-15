package com.darglk.onlineshop.controller;

import java.math.RoundingMode;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
		model.addAttribute("cart", cart);
		model.addAttribute("totalPrice", cart.totalPrice().setScale(2, RoundingMode.HALF_UP));
		return "shoppingCart";
	}
	
	@RequestMapping(value="update", method=RequestMethod.POST)
	public String updateCart(Model model, HttpServletRequest httpRequest, @RequestParam("product_ids[]") Long[] productIds,
			@RequestParam("quantity[]") Long[] quantity) {
		
		Long cartId = (Long)httpRequest.getSession().getAttribute("cart_id");
		Cart cart = cartService.updateProductQuantity(cartId, productIds, quantity);
		
		model.addAttribute("cart", cart);
		model.addAttribute("totalPrice", cart.totalPrice().setScale(2, RoundingMode.HALF_UP));
		return "shoppingCart";
	}
	
	@RequestMapping(value="removeItem/{id}", method=RequestMethod.GET)
	public String removeItemFromCart(Model model, HttpServletRequest httpRequest, @PathVariable("id") Long id) {
		Cart cart = cartService.findCart((Long)httpRequest.getSession().getAttribute("cart_id"));
		if(cart != null) {
			cartService.removeItemFromCart(id);
		}
		return "redirect:/cart/show";
	}
	
	@RequestMapping(value="clearCart", method=RequestMethod.GET)
	public String clearCart(Model model, HttpServletRequest httpRequest) {
		Long cartId = (Long)httpRequest.getSession().getAttribute("cart_id");
		cartService.clearCart(cartId);
		return "redirect:/product/list";
	}
	
	@RequestMapping(value = "/add_to_cart", method = RequestMethod.POST)
	public String addToCart(@RequestParam("product_id") Long id, Model model, HttpServletRequest httpRequest) {
		Cart cart = null;
		if(httpRequest.getSession().getAttribute("cart_id") == null) {
			cart = cartService.createCart();
			httpRequest.getSession().setAttribute("cart_id", cart.getId());
		} else {
			cart = cartService.findCart((Long)httpRequest.getSession().getAttribute("cart_id"));
		}
		cart = cartService.addItemToCart(cart.getId(), id);
		return "redirect:/cart/show";
	}
}

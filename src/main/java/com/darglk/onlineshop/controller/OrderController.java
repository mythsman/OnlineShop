package com.darglk.onlineshop.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.darglk.onlineshop.helpers.FlashMessage;
import com.darglk.onlineshop.model.Cart;
import com.darglk.onlineshop.model.Order;
import com.darglk.onlineshop.model.Shipping;
import com.darglk.onlineshop.model.User;
import com.darglk.onlineshop.service.CartService;
import com.darglk.onlineshop.service.OrderService;
import com.darglk.onlineshop.service.ShippingService;
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
	
	@Autowired
	private ShippingService shippingService;
	
	@RequestMapping(value="/hook", method=RequestMethod.POST)
	public String paypalHook(Model model, HttpServletRequest httpRequest) {
		
		httpRequest.getParameterMap().forEach((key, value) -> {
			System.out.println("Key: " + key + " values: " + Arrays.toString(value));
		});
		return "thankyou";
	}
	
	@RequestMapping(value="/checkout", method=RequestMethod.POST)
	public String checkout(Model model, HttpServletRequest httpRequest, @RequestParam("shipping") String shippingName) {
		Shipping shipping = shippingService.findByName(shippingName);
		Cart cart = cartService.findCart((Long)httpRequest.getSession().getAttribute("cart_id"));
		User user = userService.findByUsername(SecurityContextHolder.getContext()
                .getAuthentication().getName());
		Order order = orderService.placeOrder(cart, shipping, user);
		FlashMessage.createFlashMessage("info", "Your order has been placed successfully.", model);
		model.addAttribute("order", order);
		return "order_summary";
	}
	
	@RequestMapping(value = "/my_orders", method = RequestMethod.GET)
	public String loadProductsByCategory(Model model, @RequestParam(name="page", defaultValue="0", required=false) Integer page) {
		Pageable pageable = new PageRequest(page, 5);
		User user = userService.findByUsername(SecurityContextHolder.getContext()
                .getAuthentication().getName());
		Page<Order> orders = orderService.userOrders(user.getUserId(), pageable);
		model.addAttribute("orders", orders);
		model.addAttribute("page", page);
		return "my_orders";
	}
}

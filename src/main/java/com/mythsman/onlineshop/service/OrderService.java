package com.mythsman.onlineshop.service;

import java.math.BigDecimal;

import com.mythsman.onlineshop.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mythsman.onlineshop.model.Cart;
import com.mythsman.onlineshop.model.Shipping;
import com.mythsman.onlineshop.model.User;

public interface OrderService {

	Order placeOrder(Cart cart, Shipping shipping, User user);

	BigDecimal totalOrderPriceWithShipping(Order order);

	Page<Order> userOrders(Long userId, Pageable pageable);
}

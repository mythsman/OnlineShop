package com.darglk.onlineshop.service;

import java.math.BigDecimal;

import com.darglk.onlineshop.model.Cart;
import com.darglk.onlineshop.model.Order;
import com.darglk.onlineshop.model.User;

public interface OrderService {

	Order placeOrder(Cart cart, BigDecimal shipping, User user);

	BigDecimal totalOrderPriceWithShipping(Order order);

}

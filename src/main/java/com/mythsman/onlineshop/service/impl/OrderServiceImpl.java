package com.mythsman.onlineshop.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.Instant;

import javax.transaction.Transactional;

import com.mythsman.onlineshop.service.OrderService;
import com.mythsman.onlineshop.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mythsman.onlineshop.dao.CartDao;
import com.mythsman.onlineshop.dao.LineItemDao;
import com.mythsman.onlineshop.dao.OrderDao;
import com.mythsman.onlineshop.dao.OrderDetailsDao;
import com.mythsman.onlineshop.dao.ProductDao;
import com.mythsman.onlineshop.model.Cart;
import com.mythsman.onlineshop.model.LineItem;
import com.mythsman.onlineshop.model.OrderDetails;
import com.mythsman.onlineshop.model.OrderStatus;
import com.mythsman.onlineshop.model.Product;
import com.mythsman.onlineshop.model.Shipping;
import com.mythsman.onlineshop.model.User;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private OrderDetailsDao orderDetailsDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private LineItemDao lineItemDao;
	
	@Autowired
	private CartDao cartDao;
	
	@Override
	@Transactional
	public Order placeOrder(Cart cart, Shipping shipping, User user) {
		Order order = new Order(cart.totalPrice().setScale(2, RoundingMode.HALF_UP)
				, shipping, Date.from(Instant.now()),
				user, OrderStatus.BEGIN);
		setOrderedProducts(cart, order);
		return orderDao.save(order);
	}
	
	@Override
	@Transactional
	public BigDecimal totalOrderPriceWithShipping(Order order) {
		return new BigDecimal(order.getShipping().getPrice().doubleValue() + order.getTotal().doubleValue());
	}
	
	@Transactional
	private void setOrderedProducts(Cart cart, Order order) {
		double totalPrice = 0.0;
		for(LineItem lineItem : cart.getLineItems()) {
			OrderDetails orderDetails = new OrderDetails();
			Product product = lineItem.getProduct();
			
			if(product.getQuantity() <= 0L) {
				continue;
			}
			
			if(lineItem.getQuantity() >= product.getQuantity()) {
				orderDetails.setQuantity(product.getQuantity());
				product.setQuantity(0L);
			} else {
				orderDetails.setQuantity(lineItem.getQuantity());
				long diff = product.getQuantity() - lineItem.getQuantity();
				product.setQuantity(diff);
			}
			totalPrice += (product.getPrice().doubleValue() * orderDetails.getQuantity().doubleValue());
			orderDetails.setProduct(product);
			orderDetails.setOrder(order);
			lineItemDao.delete(lineItem);
			productDao.save(product);
			orderDetailsDao.save(orderDetails);
			order.getOrderDetails().add(orderDetails);
		}
		cart.getLineItems().clear();
		cartDao.save(cart);
		BigDecimal total = new BigDecimal(totalPrice);
		order.setTotal(total.setScale(2, RoundingMode.HALF_UP));
	}

	@Transactional
	@Override
	public Page<Order> userOrders(Long userId, Pageable pageable) {
		return orderDao.findByUserId(userId, pageable);
	}
}

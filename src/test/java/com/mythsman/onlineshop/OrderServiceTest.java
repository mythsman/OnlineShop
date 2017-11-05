package com.mythsman.onlineshop;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

import com.mythsman.onlineshop.service.OrderService;
import com.mythsman.onlineshop.service.impl.OrderServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.mythsman.onlineshop.dao.CartDao;
import com.mythsman.onlineshop.dao.LineItemDao;
import com.mythsman.onlineshop.dao.OrderDao;
import com.mythsman.onlineshop.dao.OrderDetailsDao;
import com.mythsman.onlineshop.dao.ProductDao;
import com.mythsman.onlineshop.model.Cart;
import com.mythsman.onlineshop.model.LineItem;
import com.mythsman.onlineshop.model.Order;
import com.mythsman.onlineshop.model.OrderDetails;
import com.mythsman.onlineshop.model.Product;
import com.mythsman.onlineshop.model.Shipping;
import com.mythsman.onlineshop.model.User;

@RunWith(SpringRunner.class)
public class OrderServiceTest {

	@TestConfiguration
	static class TestOrderServiceContextConfiguration {
		
		@Bean
		public OrderService orderService() {
			return new OrderServiceImpl();
		}
	}
	
	@Autowired
	private OrderService orderService;
	
	@MockBean
	private OrderDao orderDao;
	
	@MockBean
	private OrderDetailsDao orderDetailsDao;
	
	@MockBean
	private ProductDao productDao;
	
	@MockBean
	private LineItemDao lineItemDao;
	
	@MockBean
	private CartDao cartDao;
	
	private Order order;
	private Cart cart;
	private Shipping shipping;
	private User user;
	
	@Test
	public void testPlaceOrder() {
		cart = mock(Cart.class);
		shipping = mock(Shipping.class);
		user = mock(User.class);
		Set<LineItem> lineItems = new HashSet<>();
		Product[] products = {
			mock(Product.class),
			mock(Product.class),
			mock(Product.class)
		};
		lineItems.add(new LineItem(products[0], cart, 0L));
		lineItems.add(new LineItem(products[1], cart, 3L));
		lineItems.add(new LineItem(products[2], cart, 2L));
		when(cart.getLineItems()).thenReturn(lineItems);
		when(cart.totalPrice()).thenReturn(new BigDecimal("33.55"));
		when(orderDao.save(Mockito.any(Order.class))).thenReturn(order);
		when(products[0].getQuantity()).thenReturn(0L);
		when(products[1].getQuantity()).thenReturn(3L);
		when(products[1].getPrice()).thenReturn(new BigDecimal("11.22"));
		when(products[2].getQuantity()).thenReturn(3L);
		when(products[2].getPrice()).thenReturn(new BigDecimal("22.33"));
		
		orderService.placeOrder(cart, shipping, user);
		
		verify(products[0], times(1)).getQuantity();
		verify(products[1], times(3)).getQuantity();
		verify(products[2], times(3)).getQuantity();
		verify(products[1], times(1)).setQuantity(0L);
		verify(products[1], times(1)).getPrice();
		verify(products[2], times(1)).getPrice();
		verify(products[2], times(1)).setQuantity(1L);
		verify(lineItemDao, times(2)).delete(Mockito.any(LineItem.class));
		verify(productDao, times(2)).save(Mockito.any(Product.class));
		verify(orderDetailsDao, times(2)).save(Mockito.any(OrderDetails.class));
		verify(cartDao, times(1)).save(cart);
		verify(cart, times(2)).getLineItems();
		verify(orderDao, times(1)).save(Mockito.any(Order.class));
	}
	
	@Test
	public void testTotalOrderPriceWithShipping() {
		shipping = mock(Shipping.class);
		order = mock(Order.class);
		when(shipping.getPrice()).thenReturn(new BigDecimal("11.22"));
		when(order.getShipping()).thenReturn(shipping);
		when(order.getTotal()).thenReturn(new BigDecimal("11.22"));
		
		BigDecimal expected = new BigDecimal("22.44");
		assertTrue(expected.equals(orderService.totalOrderPriceWithShipping(order).setScale(2, RoundingMode.HALF_UP)));
	}
	
	@Test
	public void testUserOrders() {
		Pageable pageable = new PageRequest(0, 5);
		Long userId = 0L;
		orderService.userOrders(userId, pageable);
		verify(orderDao, times(1)).findByUserId(userId, pageable);
	}
}

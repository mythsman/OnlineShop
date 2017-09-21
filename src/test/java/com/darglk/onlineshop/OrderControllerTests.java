package com.darglk.onlineshop;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.darglk.onlineshop.controller.OrderController;
import com.darglk.onlineshop.model.Cart;
import com.darglk.onlineshop.model.LineItem;
import com.darglk.onlineshop.model.Order;
import com.darglk.onlineshop.model.OrderStatus;
import com.darglk.onlineshop.model.Shipping;
import com.darglk.onlineshop.model.User;
import com.darglk.onlineshop.service.CartService;
import com.darglk.onlineshop.service.OrderService;
import com.darglk.onlineshop.service.ShippingService;
import com.darglk.onlineshop.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {OrderController.class}, secure = false)
public class OrderControllerTests {

	@TestConfiguration
	static class TestOrderControllerContextConfiguration {
		@Bean
		public DefaultWebSecurityExpressionHandler expressionHandler() {
			return new DefaultWebSecurityExpressionHandler();
		}
	}
	
	@SuppressWarnings("unused")
	@Autowired
	private DefaultWebSecurityExpressionHandler handler;
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private OrderService orderService;
	
	@MockBean
	private CartService cartService;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private ShippingService shippingService;
	
	@MockBean
	private JavaMailSender mailSender;
	
	private User user;
	
	@Before
	public void setUser() {
		user = new User();
		user.setAddress("Somewhere 12");
		user.setCity("Somecity");
		user.setEmail("john@test.com");
		user.setEnabled(true);
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setPassword("aaZZa44@");
		user.setPasswordConfirmation("aaZZa44@");
		user.setPhone("700700799");
		user.setPostcode("90-691");
		user.setUsername("johndoe");
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testCheckout() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart_id", 1L);
		Shipping shipping = new Shipping("postal", new BigDecimal("19.22"));
		Cart cart = mock(Cart.class);
		Order order = mock(Order.class);
		Set<LineItem> lineItems = new HashSet<>();
		
		when(shippingService.findByName("postal")).thenReturn(shipping);
		when(cartService.findCart(1L)).thenReturn(cart);
		when(userService.findByUsername(Mockito.anyString())).thenReturn(user);
		when(orderService.placeOrder(cart, shipping, user)).thenReturn(order);
		when(cart.getLineItems()).thenReturn(lineItems);
		when(order.getId()).thenReturn(1L);
		when(order.getShipping()).thenReturn(shipping);
		when(order.getTotalWithShipping()).thenReturn(new BigDecimal("11.11"));
		when(order.getStatus()).thenReturn(OrderStatus.BEGIN);
		when(order.getUser()).thenReturn(user);
		
		mvc.perform(post("/order/checkout").session(session).param("shipping", "postal"))
		.andExpect(status().isOk()).andExpect(view().name("order_summary"));
		
		verify(mailSender, times(1)).send(Mockito.any(MimeMessage.class));
		verify(shippingService, times(1)).findByName("postal");
		verify(cartService, times(1)).findCart(1L);
		verify(userService, times(1)).findByUsername(Mockito.anyString());
		verify(cart, times(1)).getLineItems();
		verify(order, times(3)).getId();
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testShowMyOrders() throws Exception {
		List<Order> ordersList = new ArrayList<>();
		ordersList.add(new Order(new BigDecimal("11.11"), new Shipping("shipping", new BigDecimal("0.00")),
				Date.from(Instant.now()), user, OrderStatus.BEGIN));
		Page<Order> orders = new PageImpl<>(ordersList);
		
		when(userService.findByUsername(Mockito.anyString())).thenReturn(user);
		when(orderService.userOrders(eq(user.getUserId()), Mockito.any(Pageable.class))).thenReturn(orders);
		
		mvc.perform(get("/order/my_orders"))
		.andExpect(status().isOk()).andExpect(view().name("my_orders"));
		
		verify(userService, times(1)).findByUsername(Mockito.anyString());
		verify(orderService, times(1)).userOrders(eq(user.getUserId()), Mockito.any(Pageable.class));
	}
}

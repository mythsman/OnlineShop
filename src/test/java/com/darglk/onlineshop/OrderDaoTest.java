package com.darglk.onlineshop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.darglk.onlineshop.dao.OrderDao;
import com.darglk.onlineshop.model.Category;
import com.darglk.onlineshop.model.Order;
import com.darglk.onlineshop.model.OrderDetails;
import com.darglk.onlineshop.model.OrderStatus;
import com.darglk.onlineshop.model.Product;
import com.darglk.onlineshop.model.Shipping;
import com.darglk.onlineshop.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrderDaoTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private OrderDao orderDao;
	
	private Order order;
	private User user;
	private Shipping shipping;
	private OrderDetails orderDetails;
	private Product product;
	
	@Before
	public void setupOrder() {
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
		entityManager.persist(user);
		
		shipping = new Shipping("shipping", new BigDecimal("11.22"));
		entityManager.persist(shipping);
		
		Category category = new Category();
		category.setDescription("Description");
		
		category.setName("CategoryName");
		entityManager.persist(category);
		
		product = new Product("name", "abcd", 1L, new BigDecimal("11.11")
				, category, "url");
		entityManager.persist(product);
		
		orderDetails = new OrderDetails(product, order, 1L);
		entityManager.persist(orderDetails);
		
		order = new Order(product.getPrice(), shipping, Date.from(Instant.now()), user, OrderStatus.BEGIN);		
	}
	
	@Test
	public void testSavedOrderSuccessful() {
		entityManager.persist(order);
		Order found = orderDao.findOne(1L);
		assertThat(found == order);
	}
	
	@Test(expected=Exception.class)
	public void testSaveInvalidOrderThrowsException() {
		order.setTotal(null);
		order.setStatus(null);
		entityManager.persist(order);
	}
	
	@Test
	public void testGetTotalWithShipping() {
		entityManager.persist(order);
		BigDecimal expected = new BigDecimal("22.33");
		assertTrue(order.getTotalWithShipping().equals(expected));
	}
	
	@Test(expected=Exception.class)
	public void testSaveOrderWithInvalidTotalFraction() {
		order.setTotal(new BigDecimal("22.222"));
		entityManager.persist(order);
	}
}

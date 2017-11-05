package com.mythsman.onlineshop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import com.mythsman.onlineshop.dao.CartDao;
import com.mythsman.onlineshop.model.Cart;
import com.mythsman.onlineshop.model.LineItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.mythsman.onlineshop.model.Product;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CartDaoTest {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private CartDao cartDao;
	
	private LineItem lineItem;
	private Product product;
	private Cart cart;
	
	@Before
	public void setupCart() {
		cart = new Cart();
		product = new Product();
		
		product.setDescription("Description of product");
		product.setName("Product");
		BigDecimal price = new BigDecimal("111.33"); 
		product.setPrice(price);
		product.setProductImageUrl("http://sample.image.com/image.jpg");
		product.setQuantity(10L);
		
		entityManager.persist(cart);
		entityManager.persist(product);
		lineItem = new LineItem(product, cart, 1L);
		entityManager.persist(lineItem);
		cart.getLineItems().add(lineItem);
		entityManager.persist(cart);
	}
	
	@Test
	public void testFetchCartWithStoredLineItem() {
		Cart found = cartDao.getOne(cart.getId());
		assertThat(found.getLineItems().size(), is(1));
	}
	
	@Test
	public void testGetTotal() {
		Cart found = cartDao.getOne(cart.getId());
		BigDecimal expected = product.getPrice();
		BigDecimal actual = found.totalPrice();
		assertThat(actual.equals(expected));
	}
}

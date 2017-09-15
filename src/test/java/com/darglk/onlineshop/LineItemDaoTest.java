package com.darglk.onlineshop;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.darglk.onlineshop.dao.LineItemDao;
import com.darglk.onlineshop.model.Cart;
import com.darglk.onlineshop.model.LineItem;
import com.darglk.onlineshop.model.Product;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class LineItemDaoTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private LineItemDao lineItemDao;
	
	private LineItem lineItem;
	private Product product;
	private Cart cart;
	
	
	@Before
	public void setupLineItem() {
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
	}
	
	@Test
	public void testFindByProductIdAndCartIdReturnsNonNullObject() {
		entityManager.persist(lineItem);
		Long productId = product.getId();
		Long cartId = cart.getId();
		Optional<LineItem> found = lineItemDao.findByProductIdAndCartId(productId, cartId);
		assertTrue(found.get().equals(lineItem));
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testFindByProductIdAndDifferentCartIdReturnsNull() {
		Cart differentCart = new Cart();
		entityManager.persist(differentCart);
		lineItem.setCart(differentCart);
		entityManager.persist(lineItem);
		Long productId = product.getId();
		Long cartId = cart.getId();
		LineItem found = lineItemDao.findByProductIdAndCartId(productId, cartId).get();
		System.out.println(found.toString());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testFindByDifferentProductIdReturnsNull() {
		Cart differentCart = new Cart();
		entityManager.persist(differentCart);
		lineItem.setCart(differentCart);
		entityManager.persist(lineItem);
		Long productId = 4L;
		Long cartId = cart.getId();
		LineItem found = lineItemDao.findByProductIdAndCartId(productId, cartId).get();
		System.out.println(found.toString());
	}
}

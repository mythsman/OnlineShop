package com.darglk.onlineshop;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.darglk.onlineshop.dao.ProductDao;
import com.darglk.onlineshop.model.Category;
import com.darglk.onlineshop.model.Product;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TestProductDao {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private ProductDao productDao;
	
	private Product product;
	
	@Before
	public void setupCategory() {
		Category category = new Category();
		category.setDescription("Description");
		
		category.setName("CategoryName");
		entityManager.persist(category);
		
		product = new Product();
		product.setDescription("Description of product");
		product.setName("Product");
		BigDecimal price = new BigDecimal("111.33"); 
		product.setPrice(price);
		product.setProductImageUrl("http://sample.image.com/image.jpg");
		product.setQuantity(10L);
		product.setCategory(category);
	}
	
	@Test
	public void testSaveProductSuccess() {
		entityManager.persist(product);
		Product found = productDao.findOne(product.getId());
		assertTrue(found.getName().equals(product.getName()));
	}
	
	@Test(expected=Exception.class)
	public void testSaveEmptyProduct() {
		Product empty = new Product();
		entityManager.persist(empty);
	}
	
	@Test(expected=Exception.class)
	public void testSaveProductWithInvalidPrice() {
		BigDecimal price = new BigDecimal("33.333");
		product.setPrice(price);
		entityManager.persist(product);
	}
	
	@Test(expected=Exception.class)
	public void testSaveProductWithInvalidQuantity() {
		product.setQuantity(-1L);
		entityManager.persist(product);
	}
}

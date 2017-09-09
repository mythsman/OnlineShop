package com.darglk.onlineshop;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.darglk.onlineshop.dao.CategoryDao;
import com.darglk.onlineshop.model.Category;
import com.darglk.onlineshop.model.Product;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TestCategoryDao {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private CategoryDao categoryDao;
	
	private Category category;
	private List<Product> products;
	
	@Before
	public void setupCategory() {
		products = new ArrayList<Product>();
		Product product = new Product();
		product.setDescription("Description of product");
		product.setName("Product");
		BigDecimal price = new BigDecimal("111.33"); 
		product.setPrice(price);
		product.setProductImageUrl("http://sample.image.com/image.jpg");
		product.setQuantity(10L);
		product.setCategory(category);
		
		category = new Category();
		category.setDescription("Description");
		
		category.setName("CategoryName");
		
		products.add(product);
		category.setProducts(products);
		entityManager.persist(product);
	}
	
	@Test(expected=Exception.class)
	public void testSaveEmptyCategoryThrowsException() {
		Category empty = new Category();
		entityManager.persist(empty);
	}
	
	@Test(expected=Exception.class)
	public void testSaveCategoryWithTheSameNameThrowsException() {
		entityManager.persist(category);
		Category another = new Category();
		another.setDescription("Description");
		
		another.setName("CategoryName");
		entityManager.persist(another);
	}
	
	@Test
	public void testSaveCategorySuccess() {
		entityManager.persist(category);
		Category found = categoryDao.findOne(category.getId());
		assertTrue(found.getId().equals(category.getId()));
		assertThat(found.getProducts().size(), is(1));
	}
}

package com.mythsman.onlineshop;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mythsman.onlineshop.dao.ProductDao;
import com.mythsman.onlineshop.model.Category;
import com.mythsman.onlineshop.model.Product;
import com.mythsman.onlineshop.service.ProductService;
import com.mythsman.onlineshop.service.impl.ProductServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TestProductService {

	@TestConfiguration
	static class TestProductServiceContextConfiguration {
		
		@Bean
		public ProductService productService() {
			return new ProductServiceImpl();
		}
	}
	
	@Autowired
	private ProductService productService;
	
	@MockBean
	private ProductDao productDao;
	
	private List<Product> listOfProducts;
	
	@Before
	public void setupProducts() {
		listOfProducts = new ArrayList<Product>();
		Product product = new Product();
		product.setId(1L);
		product.setDescription("Description of product");
		product.setName("Product");
		BigDecimal price = new BigDecimal("111.33"); 
		product.setPrice(price);
		product.setProductImageUrl("http://sample.image.com/image.jpg");
		product.setQuantity(10L);
		
		
		Category category = new Category();
		category.setDescription("Description");
		
		category.setName("CategoryName");
		
		category.setProducts(listOfProducts);
		product.setCategory(category);
		listOfProducts.add(product);
	}
	
	@Test
	public void testFindById() {
		Product product = listOfProducts.get(0);
		when(productDao.findOne(1L)).thenReturn(product);
		Product found = productService.findById(1L);
		verify(productDao, times(1)).findOne(1L);
		assertTrue(product.getName().equals(found.getName()));
	}
	
	@Test
	public void testGetProductsByTerm() {
		Pageable pageable = new PageRequest(0, 1);
		Page<Product> page = new PageImpl<>(listOfProducts);
		when(productDao.findByTerm("product", pageable)).thenReturn(page);
		Page<Product> found = productService.getProductsByTerm("product", pageable);
		verify(productDao, times(1)).findByTerm("product", pageable);
		assertTrue(found.getContent().equals(listOfProducts));
	}
	
	@Test
	public void testGetProductsByCategoryId() {
		Pageable pageable = new PageRequest(0, 1);
		Page<Product> page = new PageImpl<>(listOfProducts);
		when(productDao.findByCategoryId(1L, pageable)).thenReturn(page);
		Page<Product> found = productService.getProductsByCategoryId((int)1L, pageable);
		verify(productDao, times(1)).findByCategoryId(1L, pageable);
		assertTrue(found.getContent().equals(listOfProducts));
	}
	
	@Test
	public void testGetSixLatestProducts() {
		
		productService.getSixLatestProducts();
		verify(productDao, times(1)).findByTerm(eq(""), Mockito.any(PageRequest.class));
	}
}

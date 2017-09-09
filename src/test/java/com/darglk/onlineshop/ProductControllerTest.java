package com.darglk.onlineshop;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.darglk.onlineshop.controller.ProductController;
import com.darglk.onlineshop.model.Category;
import com.darglk.onlineshop.model.Product;
import com.darglk.onlineshop.service.CategoryService;
import com.darglk.onlineshop.service.ProductService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {ProductController.class}, secure = false)
public class ProductControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ProductService productService;
	
	@MockBean
	private CategoryService categoryService;
	
	private Product product;
	
	@Before
	public void setProduct() {
		product = new Product();
		product.setId(1L);
		product.setDescription("Description of productDescription of productDescription of productDescription of product"
				+ "Description of productDescription of productDescription of productDescription of product"
				+ "Description of productDescription of productDescription of product");
		product.setName("Product");
		BigDecimal price = new BigDecimal("111.33"); 
		product.setPrice(price);
		product.setProductImageUrl("http://sample.image.com/image.jpg");
		product.setQuantity(10L);
		Category category = new Category();
		
		category.setDescription("Description");
		category.setName("CategoryName");
		product.setCategory(category);
	}
	
	@Test
	public void testShowProduct() throws Exception {
		when(productService.findById(1L)).thenReturn(product);
		mvc.perform(get("/product/show/1")).andExpect(status().isOk()).andExpect(view().name("showProduct"));
		verify(productService, times(1)).findById(1L);
	}
	
	@Test
	public void testListProducts() throws Exception {
		List<Product> listOfProducts = new ArrayList<>();
		listOfProducts.add(product);
		Page<Product> page = new PageImpl<>(listOfProducts);
		when(productService.getProductsByTerm(eq("search"), Mockito.any(Pageable.class))).thenReturn(page);
		mvc.perform(get("/product/list?searchTerm=search")).andExpect(status().isOk()).andExpect(view().name("home"));
		verify(productService, times(1)).getProductsByTerm(eq("search"), Mockito.any(Pageable.class));
	}
	
	@Test
	public void testListProductsByCategoryId() throws Exception {
		List<Product> listOfProducts = new ArrayList<>();
		listOfProducts.add(product);
		Page<Product> page = new PageImpl<>(listOfProducts);
		when(productService.getProductsByCategoryId(eq(1), Mockito.any(Pageable.class))).thenReturn(page);
		mvc.perform(get("/product/list/category/1?page=1")).andExpect(status().isOk()).andExpect(view().name("home"));
		verify(productService, times(1)).getProductsByCategoryId(eq(1), Mockito.any(Pageable.class));
	}
}

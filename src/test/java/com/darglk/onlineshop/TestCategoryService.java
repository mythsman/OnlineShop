package com.darglk.onlineshop;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.darglk.onlineshop.dao.CategoryDao;
import com.darglk.onlineshop.model.Category;
import com.darglk.onlineshop.service.CategoryService;
import com.darglk.onlineshop.service.CategoryServiceImpl;

@RunWith(SpringRunner.class)
public class TestCategoryService {
	
	@TestConfiguration
	static class TestCategoryServiceContextConfiguration {
		
		@Bean
		public CategoryService categoryService() {
			return new CategoryServiceImpl();
		}
	}
	
	@Autowired
	private CategoryService categoryService;
	
	@MockBean
	private CategoryDao categoryDao;
	
	private List<Category> listOfCategories;
	
	@Before
	public void setupCategories() {
		listOfCategories = new ArrayList<Category>();
		Category category = new Category();
		category.setDescription("Description");
		category.setName("CategoryName");
		listOfCategories.add(category);
	}
	
	@Test
	public void testFindCategories() {
		when(categoryDao.findAll()).thenReturn(listOfCategories);
		List<Category> found = categoryService.getCategories();
		verify(categoryDao, times(1)).findAll();
		assertTrue(found == listOfCategories);
	}
}

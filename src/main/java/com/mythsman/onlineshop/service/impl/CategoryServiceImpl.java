package com.mythsman.onlineshop.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import com.mythsman.onlineshop.service.CategoryService;
import com.mythsman.onlineshop.dao.CategoryDao;
import com.mythsman.onlineshop.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDao categoryDao;

	@Override
	@Transactional
	public List<Category> getCategories() {
		return categoryDao.findAll();
	}
	
}

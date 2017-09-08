package com.darglk.onlineshop.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darglk.onlineshop.dao.CategoryDao;
import com.darglk.onlineshop.model.Category;

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

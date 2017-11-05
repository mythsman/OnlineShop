package com.mythsman.onlineshop.service.impl;


import javax.transaction.Transactional;

import com.mythsman.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mythsman.onlineshop.dao.ProductDao;
import com.mythsman.onlineshop.model.Product;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Override
	@Transactional
	public Product findById(Long id) {
		return productDao.findOne(id);
	}

	@Override
	@Transactional
	public Page<Product> getProductsByTerm(String searchTerm, Pageable pageable) {
		return productDao.findByTerm(searchTerm, pageable);
	}

	@Override
	@Transactional
	public Page<Product> getProductsByCategoryId(Integer categoryId, Pageable pageable) {
		return productDao.findByCategoryId((long)categoryId, pageable);
	}
	
	@Override
	@Transactional
	public Page<Product> getSixLatestProducts() {
		return productDao.findByTerm("", new PageRequest(0, 6));
	}
}

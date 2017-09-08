package com.darglk.onlineshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.darglk.onlineshop.model.Product;

public interface ProductService {

	Product findById(Long id);

	Page<Product> getProductsByTerm(String searchTerm, Pageable pageable);

	Page<Product> getProductsByCategoryId(Integer page, Pageable pageable);

}

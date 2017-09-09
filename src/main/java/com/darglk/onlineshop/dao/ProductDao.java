package com.darglk.onlineshop.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.darglk.onlineshop.model.Product;

public interface ProductDao extends JpaRepository<Product, Long> {

	@Query(value="from Product p WHERE p.category.id = ?1", 
			countQuery="SELECT COUNT(p) FROM Product p WHERE p.category.id = ?1", nativeQuery=false)
	Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
	
	@Query(value="from Product p WHERE p.description LIKE %?1% OR p.name LIKE %?1%",
			countQuery="SELECT COUNT(p) FROM Product p WHERE p.description LIKE %?1% OR p.name LIKE %?1%", nativeQuery=false)
	Page<Product> findByTerm(String searchTerm, Pageable pageable);
}

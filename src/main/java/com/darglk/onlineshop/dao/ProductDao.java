package com.darglk.onlineshop.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.darglk.onlineshop.model.Product;

public interface ProductDao extends JpaRepository<Product, Long> {

	@Query(value="SELECT * FROM PRODUCT WHERE category_id = ?1 ORDER BY ?#{pageable}", 
			countQuery="SELECT COUNT(*) FROM PRODUCT WHERE category_id = ?1", nativeQuery=true)
	Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
	
	@Query(value="SELECT * FROM PRODUCT WHERE description LIKE %?1% OR name LIKE %?1% ORDER BY ?#{pageable}",
			countQuery="SELECT COUNT(*) FROM PRODUCT WHERE description LIKE %?1% OR name LIKE %?1%", nativeQuery=true)
	Page<Product> findByTerm(String searchTerm, Pageable pageable);
}

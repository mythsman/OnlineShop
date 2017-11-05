package com.mythsman.onlineshop.dao;


import com.mythsman.onlineshop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductDao extends JpaRepository<Product, Long> {

	@Query(value="from Product p WHERE p.category.id = ?1 AND p.quantity > 0", 
			countQuery="SELECT COUNT(p) FROM Product p WHERE p.category.id = ?1 AND p.quantity > 0", nativeQuery=false)
	Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
	
	@Query(value="from Product p WHERE p.quantity > 0 AND (p.description LIKE %?1% OR p.name LIKE %?1%)",
			countQuery="SELECT COUNT(p) FROM Product p WHERE p.quantity > 0 AND (p.description LIKE %?1% OR p.name LIKE %?1%)", nativeQuery=false)
	Page<Product> findByTerm(String searchTerm, Pageable pageable);
	
}

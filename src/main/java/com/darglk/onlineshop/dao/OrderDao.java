package com.darglk.onlineshop.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.darglk.onlineshop.model.Order;

public interface OrderDao extends JpaRepository<Order, Long> {

	@Query(value="from Order o WHERE o.user.id = ?1 ORDER BY o.createdAt DESC", 
			countQuery="SELECT COUNT(o) FROM Order o WHERE o.user.id = ?1 ORDER BY o.createdAt DESC", nativeQuery=false)
	Page<Order> findByUserId(Long userId, Pageable pageable);
}

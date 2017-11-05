package com.mythsman.onlineshop.dao;

import com.mythsman.onlineshop.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderDao extends JpaRepository<Order, Long> {

	@Query(value="from Order o WHERE o.user.id = ?1 ORDER BY o.createdAt DESC", 
			countQuery="SELECT COUNT(o) FROM Order o WHERE o.user.id = ?1 ORDER BY o.createdAt DESC", nativeQuery=false)
	Page<Order> findByUserId(Long userId, Pageable pageable);
}

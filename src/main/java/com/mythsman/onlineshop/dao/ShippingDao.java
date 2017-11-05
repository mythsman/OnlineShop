package com.mythsman.onlineshop.dao;

import com.mythsman.onlineshop.model.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingDao extends JpaRepository<Shipping, Long> {
	Shipping findByName(String shippingName);
}

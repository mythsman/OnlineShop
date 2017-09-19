package com.darglk.onlineshop.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.darglk.onlineshop.model.Shipping;

public interface ShippingDao extends JpaRepository<Shipping, Long> {
	Shipping findByName(String shippingName);
}

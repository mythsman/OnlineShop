package com.mythsman.onlineshop.service;

import java.util.List;

import com.mythsman.onlineshop.model.Shipping;

public interface ShippingService {
	List<Shipping> findAllShippings();
	Shipping findByName(String shippingName);
}

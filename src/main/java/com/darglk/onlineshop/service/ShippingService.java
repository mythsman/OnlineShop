package com.darglk.onlineshop.service;

import java.util.List;

import com.darglk.onlineshop.model.Shipping;

public interface ShippingService {
	List<Shipping> findAllShippings();
	Shipping findByName(String shippingName);
}

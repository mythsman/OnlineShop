package com.mythsman.onlineshop.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import com.mythsman.onlineshop.service.ShippingService;
import com.mythsman.onlineshop.dao.ShippingDao;
import com.mythsman.onlineshop.model.Shipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingServiceImpl implements ShippingService {

	@Autowired
	private ShippingDao shippingDao;
	
	@Override
	@Transactional
	public List<Shipping> findAllShippings() {
		return shippingDao.findAll();
	}

	@Override
	@Transactional
	public Shipping findByName(String shippingName) {
		return shippingDao.findByName(shippingName);
	}

}

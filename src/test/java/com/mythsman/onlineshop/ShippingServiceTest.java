package com.mythsman.onlineshop;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mythsman.onlineshop.dao.ShippingDao;
import com.mythsman.onlineshop.model.Shipping;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.mythsman.onlineshop.service.ShippingService;
import com.mythsman.onlineshop.service.impl.ShippingServiceImpl;

@RunWith(SpringRunner.class)
public class ShippingServiceTest {

	@TestConfiguration
	static class TestShippingServiceContextConfiguration {
		
		@Bean
		public ShippingService shippingService() {
			return new ShippingServiceImpl();
		}
	}
	
	@Autowired
	private ShippingService shippingService;
	
	@MockBean
	private ShippingDao shippingDao;
	
	@Test
	public void testFindAllShippings() {
		List<Shipping> listOfShippings = new ArrayList<>();
		listOfShippings.add(new Shipping("Shipping", new BigDecimal("11.22")));
		
		when(shippingDao.findAll()).thenReturn(listOfShippings);
		List<Shipping> found = shippingService.findAllShippings();
		verify(shippingDao, times(1)).findAll();
		assertTrue(listOfShippings == found);
	}
	
	@Test
	public void testFindByName() {
		Shipping shipping = new Shipping("Shipping", new BigDecimal("11.22"));
		when(shippingDao.findByName("Shipping")).thenReturn(shipping);
		Shipping found = shippingService.findByName("Shipping");
		verify(shippingDao, times(1)).findByName("Shipping");
		assertTrue(found == shipping);
	}
}

package com.darglk.onlineshop;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.darglk.onlineshop.dao.ShippingDao;
import com.darglk.onlineshop.model.Shipping;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ShippingDaoTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private ShippingDao shippingDao;
	
	private Shipping shipping;
	
	@Before
	public void setupShipping() {
		shipping = new Shipping();
		shipping.setName("shipping");
		shipping.setPrice(new BigDecimal("11.22"));
	}
	
	@Test
	public void testSaveShippingSuccessful() {
		entityManager.persist(shipping);
		Shipping found = shippingDao.findByName("shipping");
		assertTrue(found.getName().equals(shipping.getName()));
	}
	
	@Test(expected = Exception.class)
	public void testSaveShippingUnsuccessful() {
		shipping.setName(null);
		shipping.setPrice(null);
		entityManager.persist(shipping);
	}
	
	@Test(expected = Exception.class)
	public void testSaveShippingWithInvalidPriceFraction() {
		shipping.setPrice(new BigDecimal("11.222"));
		entityManager.persist(shipping);
	}
}

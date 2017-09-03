package com.darglk.onlineshop;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.darglk.onlineshop.dao.PasswordTokenDao;
import com.darglk.onlineshop.model.User;
import com.darglk.onlineshop.security.PasswordResetToken;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TestPasswordTokenDao {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private PasswordTokenDao passwordTokenDao;
	
	private PasswordResetToken passwordResetToken;
	
	private User user;
	
	@Before
	public void setupToken() {
		user = new User();
		user.setAddress("Somewhere 12");
		user.setCity("Somecity");
		user.setEmail("john@test.com");
		user.setEnabled(true);
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setPassword("aaZZa44@");
		user.setPasswordConfirmation("aaZZa44@");
		user.setPhone("700700799");
		user.setPostcode("90-691");
		user.setUsername("johndoe");
		entityManager.persist(user);
		passwordResetToken = new PasswordResetToken("3a61c1be-4550-4ac2-9492-e91adbfba40d", user);
	}
	
	@Test
	public void testPasswordTokenOneDayExpiration() {
		
		entityManager.persist(passwordResetToken);
		PasswordResetToken found = passwordTokenDao.findByToken("3a61c1be-4550-4ac2-9492-e91adbfba40d");
		assertTrue(passwordResetToken.getToken().equals(found.getToken()));
		assertTrue(passwordResetToken.getUser().equals(user));
		Date currentDate = Date.from(Instant.now());
		assertTrue(passwordResetToken.getExpiryDate().after(currentDate));
	}
}

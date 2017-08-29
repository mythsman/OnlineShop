package com.darglk.onlineshop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.darglk.onlineshop.dao.UserDao;
import com.darglk.onlineshop.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TestUserDao {

	@Autowired
	TestEntityManager entityManager;
	
	@Autowired
	private UserDao userDao;
	
	private User user;
	private User secondUser;
	
	@Before
	public void setupUser() {
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
		
		secondUser = new User();
		secondUser.setAddress("Somewhere 12");
		secondUser.setCity("Somecity");
		secondUser.setEmail("john1@test.com");
		secondUser.setEnabled(true);
		secondUser.setFirstName("John");
		secondUser.setLastName("Doe");
		secondUser.setPassword("aaZZa44@");
		secondUser.setPasswordConfirmation("aaZZa44@");
		secondUser.setPhone("701700799");
		secondUser.setPostcode("90-691");
		secondUser.setUsername("johndoe");
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void testThrowExceptionWhenAllFieldsAreEmpty() {
		User emptyUser = new User();
		entityManager.persist(emptyUser);
	}
	
	@Test
	public void testUserSaveProperly() {
		entityManager.persist(user);
		User found = userDao.findByUsername(user.getUsername());
		assertThat(found.getUsername().equals(user.getUsername()));
	}
	
	@Test(expected=Exception.class)
	public void testUserWithTheSameNameFails() {
		entityManager.persist(user);
		entityManager.persist(secondUser);
	}
}

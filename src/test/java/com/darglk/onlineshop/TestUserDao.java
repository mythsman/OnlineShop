package com.darglk.onlineshop;

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
	
	@Test(expected=Exception.class)
	public void throwExceptionIfFieldIsEmpty() {
		User user = new User();
		entityManager.persist(user);
	}
}

package com.darglk.onlineshop;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import com.darglk.onlineshop.dao.UserDao;
import com.darglk.onlineshop.model.User;
import com.darglk.onlineshop.service.UserSecurityService;



@RunWith(SpringRunner.class)
public class TestUserSecurityService {

	@TestConfiguration
	static class TestUserSecurityServiceContextConfiguration {
		
		@Bean
		public UserDetailsService userSecurityService() {
			return new UserSecurityService();
		}
	}
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@MockBean
	private UserDao userDao;
	
	private User user;
	
	@Before
	public void setupUserData() {
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
	}
	
	@Test
	public void testLoadUserByUsername() {
		when(userDao.findByUsername(user.getUsername())).thenReturn(user);
		userDetailsService.loadUserByUsername(user.getUsername());
		verify(userDao, times(1)).findByUsername(user.getUsername());
	}
	
	@Test(expected=UsernameNotFoundException.class)
	public void testLoadUserByUsernameThrowsException() {
		when(userDao.findByUsername(user.getUsername())).thenReturn(null);
		userDetailsService.loadUserByUsername(user.getUsername());
		verify(userDao, times(1)).findByUsername(user.getUsername());
	}
}

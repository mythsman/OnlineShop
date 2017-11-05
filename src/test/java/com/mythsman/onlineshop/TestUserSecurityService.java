package com.mythsman.onlineshop;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

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

import com.mythsman.onlineshop.dao.PasswordTokenDao;
import com.mythsman.onlineshop.dao.UserDao;
import com.mythsman.onlineshop.model.User;
import com.mythsman.onlineshop.security.PasswordResetToken;
import com.mythsman.onlineshop.service.UserSecurityService;



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
	
	@MockBean
	private PasswordTokenDao passwordResetTokenDao;
	
	private User user;
	
	private PasswordResetToken passwordResetToken;
	
	@Before
	public void setupUserData() {
		user = new User();
		user.setUserId(12L);
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
		passwordResetToken = new PasswordResetToken("3a61c1be-4550-4ac2-9492-e91adbfba40d", user);
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
	
	@Test
	public void testValidatePasswordWithNullResetTokenReturnsInvalidTokenMessage() {
		when(passwordResetTokenDao.findByToken("")).thenReturn(null);
		UserSecurityService userSecurityService = (UserSecurityService)userDetailsService; 
		String msg = userSecurityService.validatePasswordResetToken(0, "");
		assertTrue(msg.equals("invalidToken"));
	}
	
	@Test
	public void testValidatePasswordWithDifferentIdReturnsInvalidTokenMessage() {
		when(passwordResetTokenDao.findByToken("token")).thenReturn(passwordResetToken);
		UserSecurityService userSecurityService = (UserSecurityService)userDetailsService; 
		String msg = userSecurityService.validatePasswordResetToken(0, "token");
		assertTrue(msg.equals("invalidToken"));
	}
	
	@Test
	public void testValidatePasswordWithExpiredToken() {
		when(passwordResetTokenDao.findByToken("token")).thenReturn(passwordResetToken);
		UserSecurityService userSecurityService = (UserSecurityService)userDetailsService;
		passwordResetToken.setExpiryDate(Date.from(Instant.EPOCH));
		String msg = userSecurityService.validatePasswordResetToken(12L, "token");
		verify(passwordResetTokenDao, times(1)).delete(passwordResetToken);
		assertTrue(msg.equals("expired"));
	}
	
	@Test
	public void testValidatePasswordWithCorrectToken() {
		when(passwordResetTokenDao.findByToken("token")).thenReturn(passwordResetToken);
		UserSecurityService userSecurityService = (UserSecurityService)userDetailsService;
		passwordResetToken.setExpiryDate(Date.from(Instant.now().plus(Duration.ofDays(2))));
		String msg = userSecurityService.validatePasswordResetToken(12L, "token");
		verify(passwordResetTokenDao, times(1)).delete(passwordResetToken);
		assertTrue(msg == null);
	}
}

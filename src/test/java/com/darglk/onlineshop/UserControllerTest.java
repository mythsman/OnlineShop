package com.darglk.onlineshop;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.darglk.onlineshop.controller.UserController;
import com.darglk.onlineshop.model.User;
import com.darglk.onlineshop.service.UserSecurityService;
import com.darglk.onlineshop.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {UserController.class}, secure = false)
public class UserControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private JavaMailSender mailSender;
	
	@MockBean
	private UserSecurityService securityService;
	
	@MockBean
	private UserService userService;
	
	private User user;
	
	@Before
	public void setUser() {
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
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testInvalidateUserAccount() throws Exception {
		when(userService.findByUsername("johndoe")).thenReturn(user);
		mvc.perform(get("/user/remove")).andExpect(status().is3xxRedirection());
		verify(userService, times(1)).disableUser(user.getUsername());
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testSavePasswordWithErrorMessages() throws Exception {
		when(userService.findByUsername("johndoe")).thenReturn(user);
		mvc.perform(post("/user/savePassword")
				.param("newPassword", "password")
				.param("newPasswordConfirmation", "unequal"))
		.andExpect(status().isOk()).andExpect(view().name("updatePassword"));
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testSavePasswordSuccessful() throws Exception {
		when(userService.findByUsername("johndoe")).thenReturn(user);
		mvc.perform(post("/user/savePassword")
				.param("newPassword", "aaZZa44@")
				.param("newPasswordConfirmation", "aaZZa44@"))
		.andExpect(status().is3xxRedirection());
		verify(userService, times(1)).updateUserPassword(user);
	}
	
	@Test
	@WithAnonymousUser
	public void testShowChangePasswordPageUnsuccessful() throws Exception {
		when(securityService.validatePasswordResetToken(0, "token")).thenReturn("token");
		mvc.perform(get("/user/changePassword")
				.param("id", "0")
				.param("token", "token"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/login?lang=en"));
	}
	
	@Test
	@WithAnonymousUser
	public void testShowChangePasswordPageSuccessful() throws Exception {
		when(securityService.validatePasswordResetToken(0, "token")).thenReturn(null);
		mvc.perform(get("/user/changePassword")
				.param("id", "0")
				.param("token", "token"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/user/updatePassword"));
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testUpdateUser() throws Exception {
		when(userService.findByUsername("johndoe")).thenReturn(user);
		mvc.perform(get("/user/update"))
		.andExpect(status().isOk())
		.andExpect(view().name("signup"));
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testUpdatePassword() throws Exception {
		mvc.perform(get("/user/updatePassword"))
		.andExpect(status().isOk())
		.andExpect(view().name("updatePassword"));
	}
	
	@Test
	@WithAnonymousUser
	public void testSendResetPasswordSuccessful() throws Exception {
		when(userService.findByEmail("john@test.com")).thenReturn(user);
		mvc.perform(post("/user/resetPassword")
				.param("email", "john@test.com"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/signin"));
		
		verify(userService, times(1)).createPasswordResetTokenForUser(Mockito.any(User.class), anyString());
		verify(mailSender, times(1)).send(Mockito.any(SimpleMailMessage.class));
	}
	
	@Test
	@WithAnonymousUser
	public void testSendResetPasswordUnsuccessful() throws Exception {
		mvc.perform(post("/user/resetPassword")
				.param("email", "john@test.com"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/"));
	}
}

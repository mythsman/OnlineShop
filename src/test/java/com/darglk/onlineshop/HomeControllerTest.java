package com.darglk.onlineshop;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.darglk.onlineshop.controller.HomeController;
import com.darglk.onlineshop.dao.RoleDao;
import com.darglk.onlineshop.model.User;
import com.darglk.onlineshop.service.UserService;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {HomeController.class}, secure = false)
public class HomeControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private RoleDao roleDao;
	
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
	public void testGetRegisterUserPage() throws Exception {
		mvc.perform(get("/signup")).andExpect(status().isOk()).andExpect(view().name("signup"));
	}
	
	@Test
	@WithAnonymousUser
	public void testRegisterUser() throws Exception {
		
		mvc.perform(post("/signup")
				.param("username", "johndoe")
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("phone", "888888888")
				.param("email", "john@test.com")
				.param("address", "somewhere 12")
				.param("postcode", "12-345")
				.param("city", "City")
				.param("password", "aaZZa44@")
				.param("passwordConfirmation", "aaZZa44@").with(csrf().asHeader())
				).andExpect(status().is3xxRedirection());
	}
	
	@Test
	@WithAnonymousUser
	public void testRegisterUserFails() throws Exception {
		when(userService.checkEmailExists("john@test.com")).thenReturn(true);
		mvc.perform(post("/signup")
				.param("username", "johndoe")
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("phone", "888888888")
				.param("email", "john@test.com")
				.param("address", "somewhere 12")
				.param("postcode", "12-345")
				.param("city", "City")
				.param("password", "aaZZa44@")
				.param("passwordConfirmation", "aaZa44@").with(csrf().asHeader())
				).andExpect(view().name("signup"));
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testUpdateUser() throws Exception {
		when(userService.findByUsername("johndoe")).thenReturn(user);
		mvc.perform(post("/signup")
				.param("username", "johndoe")
				.param("firstName", "John")
				.param("lastName", "Doe")
				.param("phone", "888888888")
				.param("email", "john@test.com")
				.param("address", "somewhere 12")
				.param("postcode", "12-345")
				.param("city", "City")
				.param("password", "aaZZa44@")
				.param("passwordConfirmation", "aaZZa44@").with(csrf().asHeader())
				).andExpect(status().is3xxRedirection());
	}
	
	@Test
	public void testGetIndexPage() throws Exception {
		mvc.perform(get("/")
				.contentType(MediaType.TEXT_HTML)				
				)
				.andExpect(status().isOk()).andExpect(view().name("home"));
	}
	
	@Test
	public void testForgotPasswordPage() throws Exception {
		mvc.perform(get("/forgotPassword").contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(view().name("forgotpassword"));
	}
	
	@Test
	public void testSigninPage() throws Exception {
		mvc.perform(get("/signin").contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(view().name("signin"));
	}
}

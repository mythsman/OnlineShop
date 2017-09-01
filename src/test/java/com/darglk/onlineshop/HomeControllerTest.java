package com.darglk.onlineshop;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.darglk.onlineshop.controller.HomeController;
import com.darglk.onlineshop.dao.RoleDao;
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
	
	@Test
	public void testGetRegisterUserPage() throws Exception {
		mvc.perform(get("/signup")).andExpect(status().isOk()).andExpect(view().name("signup"));
	}
	
	@Test
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
	public void testRegisterUserFails() throws Exception {
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
	public void testGetIndexPage() throws Exception {
		mvc.perform(get("/")
				.contentType(MediaType.TEXT_HTML)				
				)
				.andExpect(status().isOk()).andExpect(view().name("home"));
	}
	
}

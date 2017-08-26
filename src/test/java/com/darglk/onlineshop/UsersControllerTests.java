package com.darglk.onlineshop;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.darglk.onlineshop.controller.UsersController;

@RunWith(SpringRunner.class)
@WebMvcTest(UsersController.class)
public class UsersControllerTests {

	@Autowired
	private MockMvc mvc;
	
	@Test
	public void testGetRegisterPage() throws Exception {
		mvc.perform(get("/users/signup")
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		.andExpect(view().name("signup"));
	}
	
	@Test
	public void testPostEmptyCredentials() throws Exception {
		MultiValueMap<String, String> postCredentials = new LinkedMultiValueMap<>();
		postCredentials.add("userName", "");
		postCredentials.add("firstName", "");
		postCredentials.add("lastName", "");
		postCredentials.add("emailAddress", "");
		postCredentials.add("password", "");
		postCredentials.add("passwordConfirmation", "");
		postCredentials.add("phoneNumber", "");
		postCredentials.add("address", "");
		postCredentials.add("city", "");
		postCredentials.add("postcode", "");
		
		mvc.perform(post("/users/signup").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.params(postCredentials)).andExpect(view().name("signup"));
	}
}

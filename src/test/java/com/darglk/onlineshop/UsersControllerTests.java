package com.darglk.onlineshop;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
}

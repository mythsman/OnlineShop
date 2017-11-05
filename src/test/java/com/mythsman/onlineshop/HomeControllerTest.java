package com.mythsman.onlineshop;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.mythsman.onlineshop.controller.HomeController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.mythsman.onlineshop.dao.RoleDao;
import com.mythsman.onlineshop.model.Product;
import com.mythsman.onlineshop.model.User;
import com.mythsman.onlineshop.service.CategoryService;
import com.mythsman.onlineshop.service.ProductService;
import com.mythsman.onlineshop.service.UserService;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {HomeController.class}, secure = false)
public class HomeControllerTest {
	
	@TestConfiguration
	static class TestHomeControllerContextConfiguration {
		@Bean
		public DefaultWebSecurityExpressionHandler expressionHandler() {
			return new DefaultWebSecurityExpressionHandler();
		}
	}
	
	@Autowired
	private DefaultWebSecurityExpressionHandler handler;
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private RoleDao roleDao;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private CategoryService categoryService;
	
	@MockBean
	private ProductService productService;
	
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
		System.out.println("USING DEFAULT WEB SECURITY EXPRESSION HANDLER: " +  handler.toString());
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
		Product[] products = Stream.generate(Product::new).limit(6).toArray(Product[]::new);
		for(Product product : products) {
			product.setPrice(new BigDecimal("11.11"));
			product.setDescription("ABCDEFABCDEFABCDEFABCDEFABCDEFABCDEFABCDEFABCDEFABCDEFABCDEFABCDEFABCD"
					+ "ABCDEFABCDEFABCDEFABCDEFABCDEFABCDEFABCDEFABCDEFABCDEFABCDEFABCDEFEF");
		}
		List<Product> listOfProducts = new ArrayList<>(Arrays.asList(products));
		Page<Product> page = new PageImpl<>(listOfProducts);
		when(productService.getSixLatestProducts()).thenReturn(page);
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

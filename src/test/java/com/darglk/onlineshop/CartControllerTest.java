package com.darglk.onlineshop;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.darglk.onlineshop.controller.CartController;
import com.darglk.onlineshop.model.Cart;
import com.darglk.onlineshop.model.LineItem;
import com.darglk.onlineshop.model.Product;
import com.darglk.onlineshop.service.CartService;
import com.darglk.onlineshop.service.ShippingService;

import org.junit.Before;
import org.junit.Test;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {CartController.class}, secure = false)
public class CartControllerTest {

	@TestConfiguration
	static class TestCartServiceContextConfiguration {
		@Bean
		public DefaultWebSecurityExpressionHandler expressionHandler() {
			return new DefaultWebSecurityExpressionHandler();
		}
	}
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private CartService cartService;
	
	@MockBean
	private ShippingService shippingService;
	
	private Cart cart;
	
	@Before
	public void setCart() {
		cart = new Cart();
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testShowCartIsNotNullButEmpty() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart_id", 1L);
		when(cartService.findCart(Mockito.anyLong())).thenReturn(cart);
		mvc.perform(get("/cart/show").session(session)).andExpect(status().is3xxRedirection());
		verify(cartService, times(1)).findCart(Mockito.anyLong());
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testShowCartIsNotNullAndNotEmpty() throws Exception {
		
		Product product = new Product();
		product.setId(1L);
		BigDecimal price = new BigDecimal("111.33"); 
		product.setPrice(price);
		product.setQuantity(1L);
		LineItem lineItem = new LineItem();
		lineItem.setQuantity(1L);
		
		cart = new Cart();
		lineItem.setCart(cart);
		lineItem.setProduct(product);
		
		cart.getLineItems().add(lineItem);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart_id", 1L);
		when(cartService.findCart(Mockito.anyLong())).thenReturn(cart);
		mvc.perform(get("/cart/show").session(session)).andExpect(status().isOk()).andExpect(view().name("shoppingCart"));
		verify(cartService, times(1)).findCart(Mockito.anyLong());
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testShowCartIsNullShouldRedirectToHomePage() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart_id", 1L);
		when(cartService.findCart(Mockito.anyLong())).thenReturn(null);
		mvc.perform(get("/cart/show").session(session)).andExpect(status().is3xxRedirection());
		verify(cartService, times(1)).findCart(Mockito.anyLong());
	}
	
	@Test(expected=IllegalArgumentException.class)
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testUpdateCartWithEmptyProductIdsShouldThrowException() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart_id", 1L);
		String[] array = {"1"};
		String[] emptyArray = {};
		mvc.perform(post("/cart/update").session(session).param("product_ids[]", emptyArray).param("quantity[]", array)
				.with(csrf().asHeader()));
	}
	
	@Test(expected=IllegalArgumentException.class)
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testUpdateCartWithEmptyQuantitiesShouldThrowException() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart_id", 1L);
		String[] array = {"1"};
		String[] emptyArray = {};
		mvc.perform(post("/cart/update").session(session).param("product_ids[]", array).param("quantity[]", emptyArray)
				.with(csrf().asHeader()));
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testUpdateCartWithNullCartIdShouldRedirect() throws Exception {
		MockHttpSession session = new MockHttpSession();
		String[] array = {"1"};
		
		mvc.perform(post("/cart/update").session(session).param("product_ids[]", array).param("quantity[]", array)
				.with(csrf().asHeader()))
		.andExpect(status().is3xxRedirection());
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testUpdateCartWithCorrectParamsSucceds() throws Exception {
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart_id", 1L);
		String[] array = {"1"};
		
		when(cartService.updateProductQuantity(eq(1L), Mockito.any(Long[].class), Mockito.any(Long[].class))).thenReturn(cart);
		mvc.perform(post("/cart/update").session(session).param("product_ids[]", array).param("quantity[]", array)
				.with(csrf().asHeader()))
		.andExpect(status().isOk()).andExpect(view().name("shoppingCart"));
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testRemoveItemFromCart() throws Exception {
		when(cartService.findCart(Mockito.anyLong())).thenReturn(cart);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart_id", 1L);
		mvc.perform(get("/cart/removeItem/1").session(session)).andExpect(status().is3xxRedirection());
		verify(cartService, times(1)).findCart(Mockito.anyLong());
		verify(cartService, times(1)).removeItemFromCart(Mockito.anyLong());
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testRemoveItemFromNullCartWontRemoveItem() throws Exception {
		when(cartService.findCart(Mockito.anyLong())).thenReturn(null);
		MockHttpSession session = new MockHttpSession();
		mvc.perform(get("/cart/removeItem/1").session(session)).andExpect(status().is3xxRedirection());
		verify(cartService, times(1)).findCart(Mockito.anyLong());
		verify(cartService, times(0)).removeItemFromCart(Mockito.anyLong());
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testClearCartSuccess() throws Exception {
		when(cartService.findCart(Mockito.anyLong())).thenReturn(cart);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart_id", 1L);
		
		mvc.perform(get("/cart/clearCart").session(session)).andExpect(status().is3xxRedirection());
		verify(cartService, times(1)).clearCart(Mockito.anyLong());
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testClearNullCartWontClear() throws Exception {
		when(cartService.findCart(Mockito.anyLong())).thenReturn(null);
		MockHttpSession session = new MockHttpSession();
		
		mvc.perform(get("/cart/clearCart").session(session)).andExpect(status().is3xxRedirection());
		verify(cartService, times(0)).clearCart(Mockito.anyLong());
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testAddToCartWithNonExistingCart() throws Exception {
		when(cartService.createCart()).thenReturn(cart);
		when(cartService.addItemToCart(Mockito.anyLong(), Mockito.anyLong())).thenReturn(cart);
		cart.setId(1L);
		mvc.perform(post("/cart/add_to_cart").param("product_id", "1")
				.with(csrf().asHeader())).andExpect(status().is3xxRedirection());
		verify(cartService, times(1)).createCart();
		verify(cartService, times(1)).addItemToCart(Mockito.anyLong(), Mockito.anyLong());
	}
	
	@Test
	@WithMockUser(username="johndoe", roles={"USER"})
	public void testAddToCartWithExistingCart() throws Exception {
		cart.setId(1L);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("cart_id", 1L);
		when(cartService.findCart(1L)).thenReturn(cart);
		when(cartService.addItemToCart(Mockito.anyLong(), Mockito.anyLong())).thenReturn(cart);
		mvc.perform(post("/cart/add_to_cart").param("product_id", "1").session(session)
				.with(csrf().asHeader())).andExpect(status().is3xxRedirection());
		verify(cartService, times(1)).findCart(1L);
		verify(cartService, times(1)).addItemToCart(Mockito.anyLong(), Mockito.anyLong());
	}
}

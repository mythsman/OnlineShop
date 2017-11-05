package com.mythsman.onlineshop;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.mythsman.onlineshop.dao.CartDao;
import com.mythsman.onlineshop.dao.LineItemDao;
import com.mythsman.onlineshop.dao.ProductDao;
import com.mythsman.onlineshop.model.Cart;
import com.mythsman.onlineshop.model.LineItem;
import com.mythsman.onlineshop.model.Product;
import com.mythsman.onlineshop.service.CartService;
import com.mythsman.onlineshop.service.impl.CartServiceImpl;

@RunWith(SpringRunner.class)
public class CartServiceTest {

	@TestConfiguration
	static class TestCartServiceContextConfiguration {
		
		@Bean
		public CartService cartService() {
			return new CartServiceImpl();
		}
	}
	
	@Autowired
	private CartService cartService;
	
	@MockBean
	private CartDao cartDao;
	
	@MockBean
	private ProductDao productDao;
	
	@MockBean
	private LineItemDao lineItemDao;	
	
	private Cart cart;
	@Test
	public void testCreateCart() {
		when(cartDao.save(Mockito.any(Cart.class))).thenReturn(Mockito.any(Cart.class));
		cartService.createCart();
		verify(cartDao, times(1)).save(Mockito.any(Cart.class));
	}
	
	@Test
	public void testAddItemToCartWhenCartIdIsNull() {
		Product product = new Product();
		product.setId(1L);
		product.setQuantity(1L);
		LineItem lineItem = new LineItem();
		lineItem.setQuantity(1L);
		
		cart = new Cart();
		lineItem.setCart(cart);
		lineItem.setProduct(product);
		Optional<LineItem> optionalLineItem = Optional.ofNullable(null);
		when(cartService.createCart()).thenReturn(cart);
		when(cartDao.save(Mockito.any(Cart.class))).thenReturn(cart);
		when(productDao.findOne(1L)).thenReturn(product);
		when(lineItemDao.findByProductIdAndCartId(1L, cart.getId())).thenReturn(optionalLineItem);
		when(lineItemDao.save(Mockito.any(LineItem.class))).thenReturn(lineItem);
		cartService.addItemToCart(null, 1L);
		
		verify(cartDao, times(1)).save(cart);
		verify(lineItemDao, times(1)).save(Mockito.any(LineItem.class));
		assertThat(lineItem.getQuantity(), is(1L));
	}
	
	@Test
	public void testAddItemToCartWhenCartIdIsNotNull() {
		Product product = new Product();
		product.setId(1L);
		product.setQuantity(1L);
		LineItem lineItem = new LineItem();
		lineItem.setQuantity(1L);
		
		cart = new Cart();
		lineItem.setCart(cart);
		lineItem.setProduct(product);
		Optional<LineItem> optionalLineItem = Optional.of(lineItem);
		when(cartDao.findOne(1L)).thenReturn(cart);
		when(cartDao.save(Mockito.any(Cart.class))).thenReturn(cart);
		when(productDao.findOne(1L)).thenReturn(product);
		when(lineItemDao.findByProductIdAndCartId(1L, 1L)).thenReturn(optionalLineItem);
		when(lineItemDao.save(Mockito.any(LineItem.class))).thenReturn(lineItem);
		cartService.addItemToCart(1L, 1L);
		
		verify(cartDao, times(1)).save(cart);
		verify(lineItemDao, times(1)).save(lineItem);
		verify(cartDao, times(1)).findOne(1L);
		assertThat(lineItem.getQuantity(), is(2L));
	}
	
	@Test
	public void testAddItemToCartWhenCartIdIsNotNullAndRequestedProductQuantityIsEqualToZero() {
		Product product = new Product();
		product.setId(1L);
		product.setQuantity(0L);
		
		cart = new Cart();
		
		when(cartDao.findOne(1L)).thenReturn(cart);
		when(cartDao.save(Mockito.any(Cart.class))).thenReturn(cart);
		when(productDao.findOne(1L)).thenReturn(product);
		
		Cart savedCart = cartService.addItemToCart(1L, 1L);
		verify(cartDao, times(1)).findOne(1L);
		assertThat(savedCart.getLineItems().size(), is(0));
	}
	
	@Test
	public void testFindCart() {
		cartService.findCart(1L);
		verify(cartDao, times(1)).findOne(1L);
	}
	
	@Test
	public void testUpdateProductQuantity() {
		Long cartId = 1L;
		Long[] productIds = {3L};
		Long[] quantities = {3L};
		Product product = new Product();
		product.setId(1L);
		product.setQuantity(1L);
		LineItem lineItem = new LineItem();
		lineItem.setQuantity(1L);
		lineItem.setProduct(product);
		Optional<LineItem> optional = Optional.of(lineItem);
		when(lineItemDao.findByProductIdAndCartId(3L, 1L)).thenReturn(optional);
		cartService.updateProductQuantity(cartId, productIds, quantities);
		verify(lineItemDao, times(1)).findByProductIdAndCartId(3L, 1L);
		verify(cartDao, times(1)).findOne(1L);
	}
	
	@Test
	public void testUpdateProductQuantityWhichIsEqualToZero() {
		Long cartId = 1L;
		Long[] productIds = {3L};
		Long[] quantities = {3L};
		Product product = new Product();
		product.setId(1L);
		product.setQuantity(0L);
		LineItem lineItem = new LineItem();
		lineItem.setQuantity(1L);
		lineItem.setProduct(product);
		Optional<LineItem> optional = Optional.of(lineItem);
		when(lineItemDao.findByProductIdAndCartId(3L, 1L)).thenReturn(optional);
		cartService.updateProductQuantity(cartId, productIds, quantities);
		verify(lineItemDao, times(1)).findByProductIdAndCartId(3L, 1L);
		verify(lineItemDao, times(1)).delete(lineItem);
		verify(cartDao, times(1)).findOne(1L);
	}
	
	@Test
	public void testUpdateProductQuantityWithNewQuantityIsLessThanProductQuantity() {
		Long cartId = 1L;
		Long[] productIds = {3L};
		Long[] quantities = {3L};
		Product product = new Product();
		product.setId(1L);
		product.setQuantity(4L);
		LineItem lineItem = new LineItem();
		lineItem.setQuantity(1L);
		lineItem.setProduct(product);
		Optional<LineItem> optional = Optional.of(lineItem);
		when(lineItemDao.findByProductIdAndCartId(3L, 1L)).thenReturn(optional);
		cartService.updateProductQuantity(cartId, productIds, quantities);
		verify(lineItemDao, times(1)).findByProductIdAndCartId(3L, 1L);
		verify(cartDao, times(1)).findOne(1L);
	}
	
	@Test
	public void testRemoveItemFromCart() {
		cartService.removeItemFromCart(1L);;
		verify(lineItemDao, times(1)).findOne(1L);
		verify(lineItemDao, times(1)).delete(Mockito.any(LineItem.class));
	}
	
	@Test
	public void testClearAndDestroyCart() {
		LineItem lineItem = new LineItem();
		Product product = new Product();
		product.setId(1L);
		lineItem.setQuantity(1L);
		
		lineItem.setCart(cart);
		lineItem.setProduct(product);
		cart = new Cart();
		cart.getLineItems().add(lineItem);
		when(cartDao.findOne(1L)).thenReturn(cart);
		
		cartService.destroyCart(1L);
		verify(lineItemDao, times(1)).delete(lineItem);
		verify(cartDao, times(1)).findOne(1L);
		assertThat(cart.getLineItems().size(), is(0));
	}
}

package com.darglk.onlineshop.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darglk.onlineshop.dao.CartDao;
import com.darglk.onlineshop.dao.LineItemDao;
import com.darglk.onlineshop.dao.ProductDao;
import com.darglk.onlineshop.model.Cart;
import com.darglk.onlineshop.model.LineItem;
import com.darglk.onlineshop.model.Product;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartDao cartDao;
	
	@Autowired
	private LineItemDao lineItemDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Override
	@Transactional
	public Cart addItemToCart(Long cartId, Long productId) {
		Product product = productDao.findOne(productId);
		Cart cart = cartId == null ? this.createCart() : cartDao.findOne(cartId);
		Optional<LineItem> lineItemOptional = lineItemDao.findByProductIdAndCartId(productId, cartId);
		LineItem lineItem = null;
		try {
			lineItem = lineItemOptional.get();
			lineItem.setQuantity(lineItem.getQuantity() + 1L);
			lineItemDao.save(lineItem);
		} catch (NoSuchElementException e) {
			lineItem = lineItemDao.save(new LineItem(product, cart, 1L));
		}
		
		cart.getLineItems().add(lineItem);
		return cartDao.save(cart);
	}
	
	@Override
	@Transactional
	public Cart createCart() {
		Cart cart = new Cart();
		return cartDao.save(cart);
	}

	@Override
	public Cart findCart(Long cartId) {
		return cartDao.findOne(cartId);
	}
}

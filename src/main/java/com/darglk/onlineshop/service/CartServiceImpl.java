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
		if (product.getQuantity() <= 0L) {
			return cart;
		}
		
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
	@Transactional
	public Cart findCart(Long cartId) {
		return cartDao.findOne(cartId);
	}

	@Override
	@Transactional
	public Cart updateProductQuantity(Long cartId, Long[] productIds, Long[] quantities) {
		
		int i = 0;
		for(Long productId : productIds) {
			LineItem lineItem = lineItemDao.findByProductIdAndCartId(productId, cartId).get();
			Product product = lineItem.getProduct();
			if((product.getQuantity() <= 0L) || (quantities[i] <= 0L)) {
				lineItemDao.delete(lineItem);
			} else {
				updateLineItemQuantity(quantities[i], lineItem, product);
			}
			i++;
		}
		return findCart(cartId);
	}

	@Override
	@Transactional
	public void removeItemFromCart(Long id) {
		LineItem lineItem = lineItemDao.findOne(id);
		lineItemDao.delete(lineItem);
	}

	@Override
	@Transactional
	public void destroyCart(Long cartId) {
		cartDao.delete(this.clearCart(cartId));
	}

	@Override
	@Transactional
	public Cart clearCart(Long cartId) {
		
		Cart cart = cartDao.findOne(cartId);
		cart.getLineItems().forEach(lineItem -> {
			lineItemDao.delete(lineItem);
		});
		cart.getLineItems().clear();
		return cart;
	}
	
	private void updateLineItemQuantity(Long quantity, LineItem lineItem, Product product) {
		if(product.getQuantity() <= quantity) {
			lineItem.setQuantity(product.getQuantity());
		} else {
			lineItem.setQuantity(quantity);
		}
	}
}

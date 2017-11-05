package com.mythsman.onlineshop.service;

import com.mythsman.onlineshop.model.Cart;

public interface CartService {

	Cart createCart();

	Cart addItemToCart(Long cartId, Long productId);

	Cart findCart(Long cartId);

	Cart updateProductQuantity(Long cartId, Long[] productIds, Long[] quantities);

	void removeItemFromCart(Long id);
	
	Cart clearCart(Long cartId);
	
	void destroyCart(Long cartId);
}

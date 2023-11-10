package org.softuni.bookshopbg.service;


import org.softuni.bookshopbg.model.entities.*;

import java.util.List;

public interface CartItemService {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	CartItem updateCartItem(CartItem cartItem);
	
	CartItem addBookToCartItem(Book book, UserEntity user, int qty);
	
	CartItem findById(Long id);

	
	CartItem save(CartItem cartItem);
	
	List<CartItem> findByOrder(Order order);

	void deleteCartItemById(Long id);
}

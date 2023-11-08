package org.softuni.bookshopbg.service;


import org.softuni.bookshopbg.model.entities.ShoppingCart;

import java.security.Principal;

public interface ShoppingCartService {
	ShoppingCart updateShoppingCart(ShoppingCart shoppingCart);

	void save(ShoppingCart shoppingCart);
	
	void clearShoppingCart(ShoppingCart shoppingCart);
}

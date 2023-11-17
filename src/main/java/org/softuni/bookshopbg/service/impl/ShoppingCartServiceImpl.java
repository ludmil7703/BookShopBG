package org.softuni.bookshopbg.service.impl;


import org.softuni.bookshopbg.model.entities.CartItem;
import org.softuni.bookshopbg.model.entities.ShoppingCart;
import org.softuni.bookshopbg.repositories.CartItemRepository;
import org.softuni.bookshopbg.repositories.ShoppingCartRepository;
import org.softuni.bookshopbg.service.CartItemService;
import org.softuni.bookshopbg.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

	private CartItemRepository cartItemRepository;
	

	private ShoppingCartRepository shoppingCartRepository;

	public ShoppingCartServiceImpl(CartItemRepository cartItemRepository, ShoppingCartRepository shoppingCartRepository) {
		this.cartItemRepository = cartItemRepository;
		this.shoppingCartRepository = shoppingCartRepository;
	}

	public ShoppingCart updateShoppingCart(ShoppingCart shoppingCart) {
		BigDecimal cartTotal = new BigDecimal(0);

		List<CartItem> cartItemList = cartItemRepository.findByShoppingCart(shoppingCart);
		for (CartItem cartItem : cartItemList) {
			if(cartItem.getBook().getInStockNumber() > 0) {
				BigDecimal bigDecimal = new BigDecimal(String.valueOf(cartItem.getBook().getOurPrice())).multiply(new BigDecimal(cartItem.getQty()));
				bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
				cartItem.setSubtotal(bigDecimal);

				cartItemRepository.save(cartItem);
				cartTotal = cartTotal.add(cartItem.getSubtotal());
			}
		}
		
		shoppingCart.setGrandTotal(cartTotal);

		shoppingCartRepository.save(shoppingCart);
		
		return shoppingCart;
	}

	@Override
	public void save(ShoppingCart shoppingCart) {
		shoppingCartRepository.save(shoppingCart);
	}

	public void clearShoppingCart(ShoppingCart shoppingCart, Principal principal) {
		List<CartItem> cartItemList = cartItemRepository.findByShoppingCart(shoppingCart);

		for (CartItem cartItem : cartItemList) {
			cartItem.setShoppingCart(null);
			cartItemRepository.save(cartItem);
		}

		shoppingCart.getCartItemList().clear();
		shoppingCart.setGrandTotal(new BigDecimal(0));

		shoppingCartRepository.save(shoppingCart);

	}

}


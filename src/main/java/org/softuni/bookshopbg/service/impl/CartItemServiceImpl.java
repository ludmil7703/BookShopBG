package org.softuni.bookshopbg.service.impl;


import org.softuni.bookshopbg.model.entities.*;
import org.softuni.bookshopbg.repositories.CartItemRepository;
import org.softuni.bookshopbg.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	private CartItemRepository cartItemRepository;


	@Override
	public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {
		return cartItemRepository.findByShoppingCart(shoppingCart);
	}

	@Override
	public CartItem updateCartItem(CartItem cartItem) {
		BigDecimal bigDecimal = new BigDecimal(String.valueOf(cartItem.getBook().getOurPrice())).multiply(new BigDecimal(cartItem.getQty()));

		bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		cartItem.setSubtotal(bigDecimal);

		cartItemRepository.save(cartItem);

		return cartItem;
	}

	@Override
	public CartItem addBookToCartItem(Book book, UserEntity user, int qty) {


		List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());

		for (CartItem cartItem : cartItemList) {
			if(book.getId() == cartItem.getBook().getId()) {
				cartItem.setQty(cartItem.getQty()+qty);
				cartItem.setSubtotal(new BigDecimal(String.valueOf(book.getOurPrice())).multiply(new BigDecimal(qty)));
				cartItemRepository.save(cartItem);
				return cartItem;
			}
		}

		CartItem cartItem = new CartItem();
		cartItem.setShoppingCart(user.getShoppingCart());
		cartItem.setBook(book);
        cartItem.getShoppingCart().setUser(user);
		cartItem.setQty(qty);
		cartItem.setSubtotal(new BigDecimal(String.valueOf(book.getOurPrice())).multiply(new BigDecimal(qty)));
		cartItem = cartItemRepository.save(cartItem);


		return cartItem;
	}

	@Override
	public CartItem findById(Long id) {
		return cartItemRepository.findById(id).orElse(null);
	}



	@Override
	public CartItem save(CartItem cartItem) {
		return cartItemRepository.save(cartItem);
	}

	@Override
	public List<CartItem> findByOrder(Order order) {
		return cartItemRepository.findByOrder(order);
	}

	@Override
	public void deleteCartItemById(Long id) {
		Optional<CartItem> cartItem = cartItemRepository.findById(id);

		if (cartItem.isPresent()) {
			Book book = cartItem.get().getBook();
			book.setInStockNumber(book.getInStockNumber() + cartItem.get().getQty());
			cartItem.get().setBook(null);
		}
		if (cartItem.isPresent()) {
			ShoppingCart shoppingCart = cartItem.get().getShoppingCart();
			shoppingCart.setGrandTotal(shoppingCart.getGrandTotal().subtract(cartItem.get().getSubtotal()));
			cartItem.get().setShoppingCart(null);
		}

		cartItemRepository.delete(cartItem.get());
	}

}
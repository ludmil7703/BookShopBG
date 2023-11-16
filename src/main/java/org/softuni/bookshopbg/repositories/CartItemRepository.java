package org.softuni.bookshopbg.repositories;


import org.softuni.bookshopbg.model.entities.CartItem;

import org.softuni.bookshopbg.model.entities.Order;
import org.softuni.bookshopbg.model.entities.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);


	List<CartItem> findByOrder(Order order);

	CartItem deleteCartItemById(Long id);
}

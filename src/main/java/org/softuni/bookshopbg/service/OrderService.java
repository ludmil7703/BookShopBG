package org.softuni.bookshopbg.service;
import org.softuni.bookshopbg.model.entities.*;

import java.util.Optional;

public interface OrderService {
	Order createOrder(ShoppingCart shoppingCart,
					  ShippingAddress shippingAddress,
					  BillingAddress billingAddress,
					  Payment payment,
					  String shippingMethod,
					  UserEntity user);
	
	Optional<Order> findById(Long id);
}

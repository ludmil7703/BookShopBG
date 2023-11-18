package org.softuni.bookshopbg.service.impl;

import lombok.AllArgsConstructor;
import org.softuni.bookshopbg.model.entities.*;
import org.softuni.bookshopbg.repositories.BookRepository;
import org.softuni.bookshopbg.repositories.CartItemRepository;
import org.softuni.bookshopbg.repositories.OrderRepository;
import org.softuni.bookshopbg.service.BookService;
import org.softuni.bookshopbg.service.CartItemService;
import org.softuni.bookshopbg.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
	

	private OrderRepository orderRepository;
	

	private CartItemRepository cartItemRepository;


	private BookRepository bookRepository;

	public OrderServiceImpl(OrderRepository orderRepository, CartItemRepository cartItemRepository, BookRepository bookRepository) {
		this.orderRepository = orderRepository;
		this.cartItemRepository = cartItemRepository;
		this.bookRepository = bookRepository;
	}

	public synchronized Order createOrder(ShoppingCart shoppingCart,
										  ShippingAddress shippingAddress,
										  BillingAddress billingAddress,
										  Payment payment,
										  String shippingMethod,
										  UserEntity user) {
		Order order = new Order();
		order.setBillingAddress(billingAddress);
		order.setOrderStatus("created");
		order.setPayment(payment);
		order.setShippingAddress(shippingAddress);
		order.setShippingMethod(shippingMethod);
		
		List<CartItem> cartItemList = cartItemRepository.findByShoppingCart(shoppingCart);
		
		for(CartItem cartItem : cartItemList) {
			Book book = cartItem.getBook();
			orderRepository.save(order);
			cartItem.setOrder(order);
			book.setInStockNumber(book.getInStockNumber() - cartItem.getQty());
			bookRepository.save(book);
			cartItemRepository.save(cartItem);
		}
		
		order.setCartItemList(cartItemList);
		order.setOrderDate(Calendar.getInstance().getTime());
		order.setOrderTotal(shoppingCart.getGrandTotal());
		shippingAddress.setOrder(order);
		billingAddress.setOrder(order);
		payment.setOrder(order);
		order.setUser(user);
		return orderRepository.save(order);
	}
	
	public Optional<Order> findById(Long id) {
		return orderRepository.findById(id);
	}

}

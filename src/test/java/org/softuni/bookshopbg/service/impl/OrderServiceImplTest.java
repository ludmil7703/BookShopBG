package org.softuni.bookshopbg.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.softuni.bookshopbg.model.entities.*;
import org.softuni.bookshopbg.repositories.BookRepository;
import org.softuni.bookshopbg.repositories.CartItemRepository;
import org.softuni.bookshopbg.repositories.OrderRepository;
import org.softuni.bookshopbg.service.BookService;
import org.softuni.bookshopbg.service.CartItemService;
import org.softuni.bookshopbg.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrderServiceImplTest {

    @Mock
    private OrderRepository mockOrderRepository;

    @Mock
    private CartItemRepository mockCartItemRepository;

    @Mock
    private BookRepository mockBookRepository;


    private OrderServiceImpl orderServiceTest;

    @BeforeEach
    void setUp() {
        orderServiceTest = new OrderServiceImpl(mockOrderRepository, mockCartItemRepository, mockBookRepository);
    }

    @AfterEach
    void tearDown() {
        orderServiceTest = null;
    }

    @Test
    void createOrder() {
       Order order = new Order();
        order.setId(1L);
        order.setOrderStatus("orderStatus");
        order.setOrderDate(null);
        order.setOrderTotal(BigDecimal.TEN);
        order.setShippingAddress(createShippingAddress());
        order.setBillingAddress(createBillingAddress());
        order.setPayment(createPayment());
        order.setShippingMethod("shippingMethod");
        order.setUser(createUser());


        CartItem cartItem = new CartItem();
        Book book = new Book();
        book.setId(1L);
        book.setInStockNumber(22);
        cartItem.setId(1L);
        cartItem.setQty(1);
        cartItem.setBook(book);
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setGrandTotal(BigDecimal.TEN);
        shoppingCart.setCartItemList(cartItemList);
        shoppingCart.setUser(createUser());

        when(mockCartItemRepository.findByShoppingCart(shoppingCart)).thenReturn(cartItemList);

        mockCartItemRepository.findByShoppingCart(shoppingCart);


        when(mockBookRepository.save(book)).thenReturn(book);
        mockBookRepository.save(book);

        when(mockCartItemRepository.save(cartItem)).thenReturn(cartItem);
        mockCartItemRepository.save(cartItem);

        when(mockOrderRepository.save(order)).thenReturn(order);
        mockOrderRepository.save(order);

        when(orderServiceTest.createOrder(shoppingCart, createShippingAddress(), createBillingAddress(), createPayment(), "shippingMethod", createUser())).thenReturn(order);

        assertEquals(order, orderServiceTest.createOrder(shoppingCart, createShippingAddress(), createBillingAddress(), createPayment(), "shippingMethod", createUser()));

    }

    @Test
    void testFindById() {
        Order order = new Order();

        when(mockOrderRepository.findById(order.getId())).thenReturn(java.util.Optional.of(order));

        Optional<Order> result = orderServiceTest.findById(order.getId());

        assertEquals(java.util.Optional.of(order), result);
    }

    private ShoppingCart createShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setGrandTotal(BigDecimal.TEN);
        CartItem cartItem = new CartItem();
        Book book = new Book();
        book.setId(1L);
        book.setInStockNumber(22);
        cartItem.setId(1L);
        cartItem.setQty(1);
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);
        shoppingCart.setCartItemList(cartItemList);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("username");
        shoppingCart.setUser(user);
        return shoppingCart;
    }

    private ShippingAddress createShippingAddress() {
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setId(1L);
        shippingAddress.setShippingAddressName("Shipping Address Name");
        return shippingAddress;
    }

    private BillingAddress createBillingAddress() {
        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setId(1L);
        billingAddress.setBillingAddressName("Billing Address Name");
        return billingAddress;
    }

    private Payment createPayment() {
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setCardName("Card Name");
        return payment;
    }

    private UserEntity createUser(){
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email");
        user.setActive(true);
        user.setRoles(null);
        user.setShoppingCart(createShoppingCart());
        user.setUserShippingList(null);
        return user;
    }

//    private Order createOrder(){
//        Order order = new Order();
//        order.setId(1L);
//        order.setOrderStatus("orderStatus");
//        order.setOrderDate(null);
//        order.setOrderTotal(BigDecimal.TEN);
//        order.setShippingAddress(createShippingAddress());
//        order.setBillingAddress(createBillingAddress());
//        order.setPayment(createPayment());
//        order.setShippingMethod("shippingMethod");
//        order.setUser(createUser());
//        return order;
//    }
}
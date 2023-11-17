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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrderServiceImplTest {

    @Mock
    private OrderRepository mockOrderRepository;

    @Mock
    private CartItemService mockCartItemService;

    @Mock
    private BookService mockBookService;


    private OrderService orderServiceTest;

    @BeforeEach
    void setUp() {
        orderServiceTest = new OrderServiceImpl(mockOrderRepository, mockCartItemService, mockBookService);
    }

    @AfterEach
    void tearDown() {
        orderServiceTest = null;
    }

    @Test
    void createOrder() {
        Order order =new Order();
        order.setId(1L);
        order.setOrderStatus("orderStatus");
        order.setShippingAddress(createShippingAddress());
        order.setBillingAddress(createBillingAddress());
        order.setPayment(createPayment());
        order.setShippingMethod("shippingMethod");
        ShoppingCart shoppingCart = createShoppingCart();
        order.setCartItemList(new ArrayList<>());

        when(mockOrderRepository.save(order)).thenReturn(order);
        when(mockCartItemService.findByShoppingCart(shoppingCart)).thenReturn(new ArrayList<>());

        Order order1 = orderServiceTest.createOrder(shoppingCart, createShippingAddress(), createBillingAddress(), createPayment(), "shippingMethod", createUser());

        assertEquals(order, order1);
        assertEquals(order.getCartItemList(), order1.getCartItemList());
        assertEquals(order.getShippingAddress(), order1.getShippingAddress());

    }

    @Test
    void testFindById() {
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
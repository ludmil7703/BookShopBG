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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void createOrderTest() {
        //Arrange
        Order order = createOrder();
        CartItem cartItem = createCartItem();
        Book book = cartItem.getBook();
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartItemList(cartItemList);
        shoppingCart.setUser(createUser());
        when(mockCartItemRepository.findByShoppingCart(shoppingCart)).thenReturn(cartItemList);
        when(mockOrderRepository.save(order)).thenReturn(order);
        when(mockBookRepository.save(book)).thenReturn(book);
        when(mockCartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(mockOrderRepository.save(order)).thenReturn(order);
        //Act
        orderServiceTest.createOrder(shoppingCart, createShippingAddress(), createBillingAddress(), createPayment(), "shippingMethod", createUser());
        //Assert
        verify(mockCartItemRepository, times(1)).findByShoppingCart(shoppingCart);
        verify(mockBookRepository, times(1)).save(book);
    }

    @Test
    void testFindById() {
        //Arrange
        Order order = new Order();
        when(mockOrderRepository.findById(order.getId())).thenReturn(java.util.Optional.of(order));
        //Act
        Optional<Order> result = orderServiceTest.findById(order.getId());
        //Assert
        assertEquals(java.util.Optional.of(order), result);
    }

    private CartItem createCartItem() {
        CartItem cartItem = new CartItem();
        Book book = new Book();
        book.setId(1L);
        book.setInStockNumber(22);
        cartItem.setId(1L);
        cartItem.setQty(1);
        cartItem.setBook(book);
        return cartItem;
    }

    private Order createOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus("Order Status");
        order.setOrderTotal(BigDecimal.TEN);
        order.setCartItemList(null);
        order.setPayment(createPayment());
        order.setBillingAddress(createBillingAddress());
        order.setShippingAddress(createShippingAddress());
        order.setShippingMethod("Shipping Method");
        order.setUser(createUser());
        return order;
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
        user.setShoppingCart(new ShoppingCart());
        user.setUserShippingList(null);
        return user;
    }

}
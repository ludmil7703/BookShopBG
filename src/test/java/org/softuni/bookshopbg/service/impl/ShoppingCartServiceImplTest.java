package org.softuni.bookshopbg.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.CartItem;
import org.softuni.bookshopbg.model.entities.ShoppingCart;
import org.softuni.bookshopbg.repositories.CartItemRepository;
import org.softuni.bookshopbg.repositories.ShoppingCartRepository;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {

    @Mock
    private CartItemRepository mockCartItemRepository;

    @Mock
    private ShoppingCartRepository mockShoppingCartRepository;

    private ShoppingCartServiceImpl shoppingCartServiceToTest;

    @BeforeEach
    void setUp() {
        shoppingCartServiceToTest = new ShoppingCartServiceImpl(mockCartItemRepository, mockShoppingCartRepository);

    }

    @AfterEach
    void tearDown() {
        shoppingCartServiceToTest = null;

    }

    @Test
    void updateShoppingCart() {
        CartItem cartItem = new CartItem();
        cartItem.setSubtotal(BigDecimal.TEN);
        cartItem.setQty(1);
        cartItem.setBook(null);
        Book book = new Book();
        book.setOurPrice(BigDecimal.TEN);
        book.setInStockNumber(1);
        cartItem.setBook(book);

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setGrandTotal(BigDecimal.TEN);
        shoppingCart.setCartItemList(new ArrayList<>());
        shoppingCart.setId(1L);
        shoppingCart.setUser(null);
        shoppingCart.setCartItemList(cartItemList);

        when(mockCartItemRepository.findByShoppingCart(shoppingCart)).thenReturn(cartItemList);
        when(mockCartItemRepository.save(cartItem)).thenReturn(cartItem);


        when(mockShoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);

        assertEquals(shoppingCart, shoppingCartServiceToTest.updateShoppingCart(shoppingCart));
        assertEquals(BigDecimal.TEN.setScale(2,BigDecimal.ROUND_HALF_UP), shoppingCart.getGrandTotal());
    }

    @Test
    void save() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setGrandTotal(BigDecimal.TEN);
        shoppingCart.setCartItemList(new ArrayList<>());

        when(mockShoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);

        shoppingCartServiceToTest.save(shoppingCart);
        verify(mockShoppingCartRepository, times(1)).save(shoppingCart);
    }

    @Test
    void clearShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setGrandTotal(BigDecimal.TEN);
        shoppingCart.setCartItemList(new ArrayList<>());

        CartItem cartItem = new CartItem();
        cartItem.setSubtotal(BigDecimal.TEN);
        cartItem.setQty(1);
        cartItem.setBook(null);
        Book book = new Book();
        book.setOurPrice(BigDecimal.TEN);
        book.setInStockNumber(1);
        cartItem.setBook(book);

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);

        shoppingCart.setCartItemList(cartItemList);

        when(mockCartItemRepository.findByShoppingCart(shoppingCart)).thenReturn(cartItemList);
        when(mockCartItemRepository.save(cartItem)).thenReturn(cartItem);

        when(mockShoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);

        shoppingCartServiceToTest.clearShoppingCart(shoppingCart, null);
        verify(mockShoppingCartRepository, times(1)).save(shoppingCart);
    }
}
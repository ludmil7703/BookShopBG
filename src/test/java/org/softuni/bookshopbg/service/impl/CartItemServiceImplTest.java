package org.softuni.bookshopbg.service.impl;

import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.CartItem;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.model.entities.ShoppingCart;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.repositories.CartItemRepository;
import org.softuni.bookshopbg.service.CartItemService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemServiceImplTest {

    @Mock
    private CartItemRepository mockCartItemRepository;

    private CartItemService cartItemServiceToTest;

    @BeforeEach
    void setUp() {
        cartItemServiceToTest = new CartItemServiceImpl(mockCartItemRepository, new ModelMapper());
    }

    @AfterEach
    void tearDown() {
        cartItemServiceToTest = null;
    }

    @Test
    void findByShoppingCart() {
        ShoppingCart shoppingCart = createShoppingCart();

        when(mockCartItemRepository.findByShoppingCart(shoppingCart))
                .thenReturn(shoppingCart.getCartItemList());

        List<CartItem> result = cartItemServiceToTest.findByShoppingCart(shoppingCart);

        assertEquals(shoppingCart.getCartItemList(), result);
        assertEquals(shoppingCart.getCartItemList().size(), result.size());


    }

    @Test
    void testUpdateCartItem() {
        CartItem cartItem = createShoppingCart().getCartItemList().get(0);

        when(mockCartItemRepository.save(cartItem))
                .thenReturn(cartItem);

        cartItem.setQty(2);
        cartItem.setSubtotal(BigDecimal.valueOf(20));

        CartItem result = cartItemServiceToTest.updateCartItem(cartItem);

        BigDecimal actualSubTotal = result.getSubtotal().setScale(0, BigDecimal.ROUND_HALF_UP);
        assertEquals(cartItem, result);
        assertEquals(2, result.getQty());
        assertEquals(BigDecimal.valueOf(20), actualSubTotal);

    }

    @Test
    void testAddBookToCartItem() {
    }

    @Test
    void findById() {
    }

    @Test
    void save() {
    }

    @Test
    void findByOrder() {
    }

    @Test
    void deleteCartItemById() {
    }

    private ShoppingCart createShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setGrandTotal(BigDecimal.TEN);
        shoppingCart.setId(1L);

        Book book = creatBook();

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setSubtotal(BigDecimal.TEN);

        shoppingCart.setCartItemList(List.of(cartItem));
        shoppingCart.setUser(null);
        return shoppingCart;
    }

    private Book creatBook() {
        {
            Book book = new Book();
            book.setId(1L);
            book.setAuthor("Author");
            book.setCategory(new Category() {{
                setCategoryName(CategoryName.ENGINEERING);
            }});
            book.setDescription("Description");
            book.setInStockNumber(1);
            book.setListPrice(BigDecimal.TEN);
            book.setOurPrice(BigDecimal.TEN);
            book.setReleaseDate(new Date());
            book.setTitle("Title");
            book.setInStockNumber(10);
            book.setImageUrl("ImageUrl");
            book.setLanguage("Language");
            book.setNumberOfPages(10);
            book.setIsbn(12345);
            return book;
        }


    }
}
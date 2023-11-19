package org.softuni.bookshopbg.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.*;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.repositories.BookRepository;
import org.softuni.bookshopbg.repositories.CartItemRepository;
import org.softuni.bookshopbg.service.CartItemService;
import org.modelmapper.ModelMapper;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceImplTest {

    @Mock
    private CartItemRepository mockCartItemRepository;

    @Mock
    private BookRepository mockBookRepository;

    @Mock
    private ModelMapper mockModelMapper;
    private  BookBindingModel bookBindingModel = new BookBindingModel();
    private Book book = new Book();

    private CartItemServiceImpl cartItemServiceToTest;

    @BeforeEach
    void setUp() {
        cartItemServiceToTest = new CartItemServiceImpl(mockCartItemRepository, mockBookRepository, mockModelMapper);
        when(mockModelMapper.map(any(), any())).thenReturn(bookBindingModel);

        bookBindingModel.setId(1L);
        bookBindingModel.setOurPrice(BigDecimal.TEN);
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
        CartItem cartItem = createShoppingCart().getCartItemList().get(0);

        UserEntity user = new UserEntity();
        user.setShoppingCart(createShoppingCart());

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);
        BookBindingModel bookBindingModel = creatBookDTO();

        when(mockBookRepository.findById(bookBindingModel.getId()))
                .thenReturn(Optional.of(cartItem.getBook()));

        when(mockCartItemRepository.findByShoppingCart(user.getShoppingCart()))
                .thenReturn(cartItemList);

        when(mockBookRepository.save(cartItem.getBook()))
                .thenReturn(cartItem.getBook());


//        when(mockCartItemRepository.save(cartItem))
//                .thenReturn(cartItem);

        when(cartItemServiceToTest.addBookToCartItem(creatBookDTO(), user, 1))
                .thenReturn(cartItem);
        verify(cartItemServiceToTest, times(1)).addBookToCartItem(creatBookDTO(), user, 1);

    }

    @Test
    void testFindById() {
        CartItem cartItem = createShoppingCart().getCartItemList().get(0);

        when(mockCartItemRepository.findById(cartItem.getId()))
                .thenReturn(Optional.of(cartItem));

        CartItem result = cartItemServiceToTest.findById(cartItem.getId());

        assertEquals("Author", result.getBook().getAuthor());
    }

    @Test
    void testSaveCartItem() {
        CartItem cartItem = createShoppingCart().getCartItemList().get(0);

        when(mockCartItemRepository.save(cartItem))
                .thenReturn(cartItem);

        CartItem result = cartItemServiceToTest.save(cartItem);

        assertEquals(cartItem, result);
    }

    @Test
    void testFindByOrder() {
        List<CartItem> cartItemList = createShoppingCart().getCartItemList();

        Order order = createOrder();

        when(mockCartItemRepository.findByOrder(order))
                .thenReturn(cartItemList);

        List<CartItem> result = cartItemServiceToTest.findByOrder(order);

        assertEquals(cartItemList, result);
    }



    @Test
    void testDeleteCartItemById() {
        CartItem cartItem = createCartItem();

        when(mockCartItemRepository.findById(cartItem.getId()))
                .thenReturn(Optional.of(cartItem));

        when(mockCartItemRepository.deleteCartItemById(cartItem.getId()))
                .thenReturn(cartItem);

        cartItemServiceToTest.deleteCartItemById(cartItem.getId());

        verify(mockCartItemRepository, times(1)).deleteCartItemById(cartItem.getId());
    }

    private CartItem createCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setQty(1);
        cartItem.setSubtotal(BigDecimal.TEN);
        cartItem.setBook(creatBook());
        cartItem.setShoppingCart(createShoppingCart());
        return cartItem;
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

    private Order createOrder() {
        Order order = new Order();
        order.setOrderStatus("OrderStatus");
        return order;
    }

    private Book creatBook() {

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

        private BookBindingModel creatBookDTO() {
            BookBindingModel book = new BookBindingModel();
            book.setAuthor("Author");
            book.setCategory(CategoryName.ENGINEERING);
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
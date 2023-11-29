package org.softuni.bookshopbg.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.*;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.repositories.BookRepository;
import org.softuni.bookshopbg.repositories.CartItemRepository;
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

    @Mock
    private  BookBindingModel mockBookBindingModel;


    @InjectMocks
    private CartItemServiceImpl cartItemServiceToTest;

    @BeforeEach
    void setUp() {
        cartItemServiceToTest = new CartItemServiceImpl(mockCartItemRepository, mockBookRepository, mockModelMapper);
        mockBookBindingModel.setId(1L);
        mockBookBindingModel.setOurPrice(BigDecimal.TEN);
    }

    @AfterEach
    void tearDown() {
        cartItemServiceToTest = null;
    }

    @Test
    void findByShoppingCart() {
        // Arrange
        ShoppingCart shoppingCart = createShoppingCart();
        when(mockCartItemRepository.findByShoppingCart(shoppingCart))
                .thenReturn(shoppingCart.getCartItemList());
        // Act
        List<CartItem> result = cartItemServiceToTest.findByShoppingCart(shoppingCart);
        // Assert
        assertEquals(shoppingCart.getCartItemList(), result);
        assertEquals(shoppingCart.getCartItemList().size(), result.size());
    }

    @Test
    void testUpdateCartItem() {
        // Arrange
        CartItem cartItem = createShoppingCart().getCartItemList().get(0);
        cartItem.setQty(2);
        cartItem.setSubtotal(BigDecimal.valueOf(20));
        when(mockCartItemRepository.save(cartItem))
                .thenReturn(cartItem);
        // Act
        CartItem result = cartItemServiceToTest.updateCartItem(cartItem);
        BigDecimal actualSubTotal = result.getSubtotal().setScale(0, BigDecimal.ROUND_HALF_UP);
        // Assert
        assertEquals(cartItem, result);
        assertEquals(2, result.getQty());
        assertEquals(BigDecimal.valueOf(20), actualSubTotal);
    }

    @Test
    void testAddBookToCartItemTest() {
        // Arrange
        BookBindingModel bookBindingModel = creatBookDTO();
        UserEntity user = createUser();
        Book book = creatBook();
        int qty = 2;
        CartItem existingCartItem = createCartItem();
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(existingCartItem);
        // Mock behavior of the repository
        when(mockCartItemRepository.findByShoppingCart(user.getShoppingCart())).thenReturn(cartItemList);
        when(mockCartItemRepository.save(cartItemList.get(0))).thenReturn(existingCartItem);
        when(mockModelMapper.map(bookBindingModel, Book.class)).thenReturn(book);
        //Act
        CartItem resultCartItem = cartItemServiceToTest.addBookToCartItem(bookBindingModel, user, qty);
        // Verify the result
        assertNotNull(resultCartItem);
        // Verify that the repository methods were called as expected
        verify(mockCartItemRepository, times(1)).findByShoppingCart(user.getShoppingCart());
        verify(mockCartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void  addBookToCartItemWithEmptyCartItemListTest(){
        // Arrange
        BookBindingModel bookBindingModel = creatBookDTO();
        Book book = creatBook();
        when(mockCartItemRepository.findByShoppingCart(any(ShoppingCart.class))).thenReturn(new ArrayList<>());
        when(mockModelMapper.map(bookBindingModel, Book.class)).thenReturn(book);
        when(mockCartItemRepository.save(any(CartItem.class))).thenReturn(createCartItem());
        //Act
        cartItemServiceToTest.addBookToCartItem(bookBindingModel, new UserEntity(), 1);
        //Assert
        verify(mockCartItemRepository, times(1)).findByShoppingCart(any(ShoppingCart.class));
        verify(mockModelMapper, times(1)).map(bookBindingModel, Book.class);
    }

    @Test
    void testFindById() {
        // Arrange
        CartItem cartItem = createShoppingCart().getCartItemList().get(0);
        when(mockCartItemRepository.findById(cartItem.getId()))
                .thenReturn(Optional.of(cartItem));
        // Act
        CartItem result = cartItemServiceToTest.findById(cartItem.getId());
        // Assert
        assertEquals("Author", result.getBook().getAuthor());
    }

    @Test
    void testSaveCartItem() {
        // Arrange
        CartItem cartItem = createShoppingCart().getCartItemList().get(0);
        when(mockCartItemRepository.save(cartItem))
                .thenReturn(cartItem);
        // Act
        CartItem result = cartItemServiceToTest.save(cartItem);
        // Assert
        assertEquals(cartItem, result);
    }

    @Test
    void testFindByOrder() {
        // Arrange
        List<CartItem> cartItemList = createShoppingCart().getCartItemList();
        Order order = createOrder();
        when(mockCartItemRepository.findByOrder(order))
                .thenReturn(cartItemList);
        // Act
        List<CartItem> result = cartItemServiceToTest.findByOrder(order);
        // Assert
        assertEquals(cartItemList, result);
    }

    @Test
    void testDeleteCartItemById() {
        // Arrange
        CartItem cartItem = createCartItem();
        when(mockCartItemRepository.findById(cartItem.getId()))
                .thenReturn(Optional.of(cartItem));
        doNothing().when(mockCartItemRepository).delete(cartItem);
        // Act
        cartItemServiceToTest.deleteCartItemById(cartItem.getId());
        // Assert
        verify(mockCartItemRepository, times(1)).delete(cartItem);
    }
    @Test
    void testDeleteCartItemByIdWithNull() {
        //Arrange
        CartItem cartItem = createCartItem();
        //Act and Assert
        assertThrows(IllegalArgumentException.class, () -> cartItemServiceToTest.deleteCartItemById(cartItem.getId()));
    }

    @Test
    void testDeleteCartItemByIdWithNullShoppingCart() {
        //Arrange
        CartItem cartItem = createCartItem();
        when(mockCartItemRepository.findById(cartItem.getId()))
                .thenReturn(Optional.of(cartItem));
        //Act
       cartItemServiceToTest.deleteCartItemById(cartItem.getId());
        //Assert
        verify(mockCartItemRepository, times(1)).delete(cartItem);
    }

    private UserEntity createUser() {
        UserEntity user = new UserEntity();
        user.setShoppingCart(new ShoppingCart());
        user.getShoppingCart().setCartItemList(new ArrayList<>());
        user.getShoppingCart().setGrandTotal(BigDecimal.ZERO);
        user.getShoppingCart().setUser(user);
        user.setId(1L);
        return user;
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
            book.setId(1L);
            book.setCartItems(new ArrayList<>());
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
            book.setId(1L);
            return book;
        }

}
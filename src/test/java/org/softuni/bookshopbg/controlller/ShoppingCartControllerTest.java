package org.softuni.bookshopbg.controlller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.CartItem;
import org.softuni.bookshopbg.model.entities.ShoppingCart;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.service.impl.BookServiceImpl;
import org.softuni.bookshopbg.service.impl.CartItemServiceImpl;
import org.softuni.bookshopbg.service.impl.ShoppingCartServiceImpl;
import org.softuni.bookshopbg.service.impl.UserServiceImpl;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingCartControllerTest {

    @Mock
    private BookServiceImpl mockBookService;
    @Mock
    private CartItemServiceImpl mockCartItemService;
    @Mock
    private UserServiceImpl mockUserService;

    @Mock
    private ShoppingCartServiceImpl mockShoppingCartService;

    @Mock
    private Model mockModel;

    @Mock
    private Principal mockPrincipal;

    @Mock
    private MockMvc mockMvc;

    @Mock
    BookBindingModel mockBookBindingModel;

    @Mock
    CartItem mockCartItem;


    private ShoppingCartController shoppingCartControllerUnderTest;

    @BeforeEach
    void setUp() {
        shoppingCartControllerUnderTest = new ShoppingCartController(mockUserService, mockCartItemService, mockBookService, mockShoppingCartService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(new ShoppingCartController(mockUserService, mockCartItemService, mockBookService, mockShoppingCartService)).build();
           Mockito.reset(mockBookService);
              Mockito.reset(mockCartItemService);
                Mockito.reset(mockUserService);
                Mockito.reset(mockShoppingCartService);

    mockBookBindingModel = new BookBindingModel();
    mockBookBindingModel.setId(1L);

    mockCartItem = new CartItem();
    mockCartItem.setId(1L);
    mockCartItem.setQty(1);
    }

    @AfterEach
    void tearDown() {
        shoppingCartControllerUnderTest = null;
    }

    @Test
    void shoppingCartTest() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(mockCartItem);
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(mockPrincipal.getName());
        userEntity.setShoppingCart(shoppingCart);
        when(mockUserService.findUserByUsername(mockPrincipal.getName()))
                .thenReturn((userEntity));


        when(mockCartItemService.findByShoppingCart(shoppingCart))
                .thenReturn(cartItemList);

        when(mockShoppingCartService.updateShoppingCart(shoppingCart))
                .thenReturn(shoppingCart);

        RequestBuilder request = get("/shoppingCart/cart")
                .principal(mockPrincipal);

        mockMvc.perform(request)
                .andExpect(view().name("shoppingCart"))
                .andExpect(model().attributeExists("cartItemList"))
                .andExpect(model().attributeExists("shoppingCart"))
                .andExpect(model().attributeExists("user"))
                .andExpect(status().isOk());
    }

    @Test
    void shoppingCartTestWithEmptyCart() throws Exception {

        ShoppingCart shoppingCart = new ShoppingCart();

        List<CartItem> cartItemList = new ArrayList<>();

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(mockPrincipal.getName());
        userEntity.setShoppingCart(shoppingCart);

        when(mockUserService.findUserByUsername(mockPrincipal.getName()))
                .thenReturn(userEntity);

        when(mockCartItemService.findByShoppingCart(shoppingCart))
                .thenReturn(cartItemList);

        when(mockShoppingCartService.updateShoppingCart(shoppingCart))
                .thenReturn(shoppingCart);

        RequestBuilder request = get("/shoppingCart/cart")
                .principal(mockPrincipal);

        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("forward:/"));

    }

    @Test
    void addCartItemTestWithNotEnoughStock() throws Exception {

       UserEntity userEntity = new UserEntity();
         userEntity.setUsername(mockPrincipal.getName());
        Book book = new Book();
        book.setId(1L);
        book.setInStockNumber(1);

        when(mockUserService.findUserByUsername(mockPrincipal.getName()))
                .thenReturn(userEntity);
        when(mockBookService.findBookById(1L))
                .thenReturn(book);


        RequestBuilder request = MockMvcRequestBuilders.post("/shoppingCart/addItem")
                .param("id", "1")
                .param("qty", "2")
                .principal(mockPrincipal);

        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("forward:/bookDetail/1"));

    }

    @Test
    void addCartItemTest() throws Exception {

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(mockPrincipal.getName());
        Book book = new Book();
        book.setId(1L);
        book.setInStockNumber(10);

        when(mockUserService.findUserByUsername(mockPrincipal.getName()))
                .thenReturn(userEntity);
        when(mockBookService.findBookById(1L))
                .thenReturn(book);

        RequestBuilder request = MockMvcRequestBuilders.post("/shoppingCart/addItem")
                .param("id", "1")
                .param("qty", "2")
                .principal(mockPrincipal);

        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("forward:/bookDetail/1"));

    }

    @Test
    void updateShoppingCart() throws Exception {
        when(mockCartItemService.findById(1L))
                .thenReturn(mockCartItem);

        when(mockCartItemService.updateCartItem(mockCartItem))
                .thenReturn(mockCartItem);
        RequestBuilder request = MockMvcRequestBuilders.post("/shoppingCart/updateCartItem")
                .param("id", "1")
                .param("qty", "1");

        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("forward:/shoppingCart/cart"));
    }

    @Test
    void removeItemTest() {
        when(mockCartItemService.deleteCartItemById(1L))
                .thenReturn(mockCartItem);

        String result = shoppingCartControllerUnderTest.removeItem(1L);

        assertEquals("forward:/shoppingCart/cart", result);
    }
}
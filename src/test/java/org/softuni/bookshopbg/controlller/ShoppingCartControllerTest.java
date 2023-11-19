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
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.service.impl.BookServiceImpl;
import org.softuni.bookshopbg.service.impl.CartItemServiceImpl;
import org.softuni.bookshopbg.service.impl.ShoppingCartServiceImpl;
import org.softuni.bookshopbg.service.impl.UserServiceImpl;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    }

    @AfterEach
    void tearDown() {
        shoppingCartControllerUnderTest = null;
    }

    @Test
    void shoppingCartTest() throws Exception {

        ShoppingCart shoppingCart = new ShoppingCart();
        CartItem cartItem = new CartItem();
        cartItem.setQty(1);
        cartItem.setShoppingCart(shoppingCart);

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);

        shoppingCart.setCartItemList(cartItemList);


        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(mockPrincipal.getName());
        userEntity.setShoppingCart(shoppingCart);
        when(mockUserService.findUserByUsername(mockPrincipal.getName()))
                .thenReturn(Optional.of(userEntity));


        when(mockCartItemService.findByShoppingCart(shoppingCart))
                .thenReturn(cartItemList);

        when(mockShoppingCartService.updateShoppingCart(shoppingCart))
                .thenReturn(shoppingCart);


        String result = shoppingCartControllerUnderTest.shoppingCart(mockModel, mockPrincipal);


        assertEquals("shoppingCart", result);


        RequestBuilder request = get("/shoppingCart/cart")
                .principal(mockPrincipal);

        mockMvc.perform(request)
                .andExpect(view().name("shoppingCart"))
                .andExpect(model().attributeExists("cartItemList"))
                .andExpect(model().attributeExists("shoppingCart"))
                .andExpect(status().isOk());
    }

    @Test
    void shoppingCartTestWithEmptyCart() {

        ShoppingCart shoppingCart = new ShoppingCart();

        List<CartItem> cartItemList = new ArrayList<>();


        shoppingCart.setCartItemList(cartItemList);
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(mockPrincipal.getName());
        userEntity.setShoppingCart(shoppingCart);
        when(mockUserService.findUserByUsername(mockPrincipal.getName()))
                .thenReturn(Optional.of(userEntity));

        when(mockCartItemService.findByShoppingCart(shoppingCart))
                .thenReturn(cartItemList);

        when(mockShoppingCartService.updateShoppingCart(shoppingCart))
                .thenReturn(shoppingCart);
        String result = shoppingCartControllerUnderTest.shoppingCart(mockModel, mockPrincipal);


        assertEquals("forward:/", result);

    }

    @Test
    void addCartItemTestWithNotEnoughStock() throws Exception {

       UserEntity userEntity = new UserEntity();
         userEntity.setUsername(mockPrincipal.getName());
        Book book = new Book();
        book.setId(1L);
        book.setInStockNumber(1);

        when(mockUserService.findUserByUsername(mockPrincipal.getName()))
                .thenReturn(Optional.of(userEntity));
        when(mockBookService.findBookById(1L))
                .thenReturn(book);

//        when(mockCartItemService.addBookToCartItem(mockBookBindingModel, userEntity, 1))
//                .getMock();


        String result = shoppingCartControllerUnderTest.addItem(mockBookBindingModel, "1", mockModel, mockPrincipal);

        assertEquals("forward:/bookDetail/1", result);

    }

    @Test
    void addCartItemTest() throws Exception {
        mockBookBindingModel.setInStockNumber(2);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(mockPrincipal.getName());
        Book book = new Book();
        book.setId(1L);
        book.setInStockNumber(10);

        when(mockUserService.findUserByUsername(mockPrincipal.getName()))
                .thenReturn(Optional.of(userEntity));
        when(mockBookService.findBookById(1L))
                .thenReturn(book);

//        when(mockCartItemService.addBookToCartItem(mockBookBindingModel, userEntity, 1))
//                .getMock();

        String result = shoppingCartControllerUnderTest.addItem(mockBookBindingModel, "1", mockModel, mockPrincipal);

        assertEquals("forward:/bookDetail/1", result);

    }

    @Test
    void updateShoppingCart() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/shoppingCart/updateShoppingCart")
                .param("id", "1")
                .param("qty", "1");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/shoppingCart/cart"));
    }

    @Test
    void removeItem() {
    }
}
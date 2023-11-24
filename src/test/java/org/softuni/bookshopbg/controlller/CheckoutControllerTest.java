package org.softuni.bookshopbg.controlller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.softuni.bookshopbg.model.entities.*;
import org.softuni.bookshopbg.service.*;
import org.softuni.bookshopbg.utils.MailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", roles = {"USER", "ADMIN"})
class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private final ShippingAddress mockShippingAddress = new ShippingAddress();
    @Mock
    private final BillingAddress mockBillingAddress = new BillingAddress();
    @Mock
    private final Payment mockPayment = new Payment();
    @Mock
    private CategoryService mockCategoryService;
    @Mock
    private JavaMailSender mockMailSender;
    @Mock
    private MailConstructor mockMailConstructor;
    @Mock
    private UserService mockUserService;
    @Mock
    private CartItemService mockCartItemService;
    @Mock
    private ShoppingCartService mockShoppingCartService;
    @Mock
    private ShippingAddressService mockShippingAddressService;
    @Mock
    private BillingAddressService mockBillingAddressService;
    @Mock
    private PaymentService mockPaymentService;
    @Mock
    private UserShippingService mockUserShippingService;
    @Mock
    private UserPaymentService mockUserPaymentService;
    @Mock
    private OrderService mockOrderService;



    @Mock
    private UserEntity mockUser;

    @Mock
    private Principal mockPrincipal;



    private  CheckoutController checkoutControllerUnderTest;




    @BeforeEach
    void setUp() {
        checkoutControllerUnderTest = new CheckoutController(mockCategoryService,
                mockMailSender,
                mockMailConstructor,
                mockUserService,
                mockCartItemService,
                mockShoppingCartService,
                mockShippingAddressService,
                mockBillingAddressService,
                mockPaymentService,
                mockUserShippingService,
                mockUserPaymentService,
                mockOrderService);

        Mockito.reset(mockCategoryService,
                mockMailSender,
                mockMailConstructor,
                mockUserService,
                mockCartItemService,
                mockShoppingCartService,
                mockShippingAddressService,
                mockBillingAddressService,
                mockPaymentService,
                mockUserShippingService,
                mockUserPaymentService,
                mockOrderService);

    }

    @AfterEach
    void tearDown() {
        checkoutControllerUnderTest = null;
    }

    @Test
    @WithMockUser(username = "test", password = "1234", roles = {"USER", "ADMIN"})
    void checkoutWrongCartId() throws Exception {
        mockUser = getTestUser();

        Category category = new Category();
        category.setId(1L);

        Book book = new Book();

        book.setId(1L);
        book.setCategory(category);

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setBook(book);


        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setCartItemList(new ArrayList<>());
        shoppingCart.getCartItemList().add(cartItem);
        shoppingCart.setUser(mockUser);


        when(mockPrincipal.getName()).thenReturn(mockUser.getUsername());

        when(mockUserService.findUserByUsername(mockPrincipal.getName())).thenReturn(mockUser);

        when(mockPrincipal.getName()).thenReturn(mockUser.getUsername());

when(mockUserService.findUserByUsername(mockPrincipal.getName())).thenReturn(mockUser);

RequestBuilder requestBuilder = get("/checkout")
                .param("id", "1")
                .param("cartId", "2")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("error"));
    }

    @Test
    void checkoutPost() throws Exception {
        ShoppingCart mockSh = new ShoppingCart();
        mockSh.setId(1L);
        mockSh.setGrandTotal(BigDecimal.valueOf(1.0));

        CartItem mockCartItem = new CartItem();
        mockCartItem.setId(1L);
        mockCartItem.setQty(1);
        mockCartItem.setSubtotal(BigDecimal.valueOf(1.0));
        mockCartItem.setBook(new Book());
        mockCartItem.setShoppingCart(mockSh);

        mockSh.setCartItemList(List.of(mockCartItem));

        mockUser = getTestUser();
        mockUser.setShoppingCart(mockSh);

        when(mockPrincipal.getName()).thenReturn(mockUser.getUsername());
        when( mockUserService.findUserByUsername(mockPrincipal.getName())).thenReturn(mockUser);
        when(mockUserService.findById(mockUser.getId())).thenReturn(mockUser);
        when(mockUser.getShoppingCart()).thenReturn(mockSh);
        when(mockUserService.findById(mockUser.getId())).thenReturn(mockUser);


        mockShippingAddress.setId(1L);

        when(mockShippingAddressService.setByUserShipping(any(), any())).thenReturn(mockShippingAddress);

        when(mockCartItemService.findByShoppingCart(any())).thenReturn(new ArrayList<>());

        RequestBuilder requestBuilder = post("/checkout")
                .param("id", "1")
                .param("cartId", "1")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/checkout?id=1&cartId=1"));

    }

    @Test
    void setShippingAddress() {
    }

    @Test
    void setPaymentMethod() {
    }

    private UserEntity getTestUser() {
        mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setUsername("test");
        mockUser.setPassword("1234");
        mockUser.setEmail("test@test.com");
        mockUser.setFirstName("Test");

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(mockUser);

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setBook(new Book());

        ArrayList<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        shoppingCart.setCartItemList(cartItems);
        mockUser.setShoppingCart(shoppingCart);

        return mockUser;
    }
}


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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        mockMvc = MockMvcBuilders.standaloneSetup(checkoutControllerUnderTest).build();
    }

    @AfterEach
    void tearDown() {
        checkoutControllerUnderTest = null;
    }

    @Test
    @WithMockUser(username = "test", password = "1234", roles = {"USER", "ADMIN"})
    void checkoutWrongCartId() throws Exception {
        UserEntity mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setUsername("test");
        mockUser.setPassword("1234");
        mockUser.setEmail("test");
        mockUserService.save(mockUser);
        when(mockPrincipal.getName()).thenReturn("test");
        when(mockUserService.findUserByUsername("test")).thenReturn(mockUser);

        Category category = new Category();
        category.setId(1L);
        when(mockCategoryService.getAllCategories()).thenReturn(List.of(category));

        when(mockCartItemService.findByShoppingCart(any())).thenReturn(new ArrayList<>());



RequestBuilder requestBuilder = get("/checkout")
                .param("id", "1")
                .param("missingRequiredField", "false")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    void  checkoutEmptyCartTest() throws Exception {
        UserEntity mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setUsername("test");
        mockUser.setPassword("1234");
        mockUser.setEmail("test");

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);

        shoppingCart.setUser(mockUser);
        mockUser.setShoppingCart(shoppingCart);

        when(mockPrincipal.getName()).thenReturn("test");
        when(mockUserService.findUserByUsername("test")).thenReturn(mockUser);

        when(mockCartItemService.findByShoppingCart(any())).thenReturn(new ArrayList<>());

        RequestBuilder requestBuilder = get("/checkout")
                .param("id", "1")
                .param("missingRequiredField", "false")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("emptyCart"))
                .andExpect(view().name("forward:/shoppingCart/cart"));
    }



    @Test
    void checkoutWithINotIBookInStock() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(userEntity).when(mockUserService).findUserByUsername("test");


        ShoppingCart mockSh = new ShoppingCart();
        mockSh.setId(1L);
        mockSh.setGrandTotal(BigDecimal.valueOf(1.0));

        CartItem mockCartItem = new CartItem();
        mockCartItem.setId(1L);
        mockCartItem.setQty(3);
        mockCartItem.setSubtotal(BigDecimal.valueOf(1.0));
        Book mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setInStockNumber(1);
        mockCartItem.setBook(mockBook);
        mockCartItem.setShoppingCart(mockSh);

        mockSh.setCartItemList(List.of(mockCartItem));
        userEntity.setShoppingCart(mockSh);


        when(mockCartItemService.findByShoppingCart(userEntity.getShoppingCart())).thenReturn(List.of(mockCartItem));

        RequestBuilder requestBuilder = get("/checkout")
                .param("id", "1")
                .param("cartId", "1")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("notEnoughStock"))
                .andExpect(view().name("forward:/shoppingCart/cart"));

    }
    @Test
    void checkoutWithIBookInStockTest() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(userEntity).when(mockUserService).findUserByUsername("test");

        ShoppingCart mockSh = new ShoppingCart();
        mockSh.setId(1L);
        mockSh.setGrandTotal(BigDecimal.valueOf(1.0));

        CartItem mockCartItem = new CartItem();
        mockCartItem.setId(1L);
        mockCartItem.setQty(3);
        mockCartItem.setSubtotal(BigDecimal.valueOf(1.0));
        Book mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setInStockNumber(22);
        mockCartItem.setBook(mockBook);
        mockCartItem.setShoppingCart(mockSh);

        mockSh.setCartItemList(List.of(mockCartItem));
        userEntity.setShoppingCart(mockSh);

        when(mockCartItemService.findByShoppingCart(userEntity.getShoppingCart())).thenReturn(List.of(mockCartItem));

        RequestBuilder requestBuilder = get("/checkout")
                .param("id", "1")
                .param("cartId", "1")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(model().attributeExists("userShippingList"))
                .andExpect(model().attributeExists("userPaymentList"))
                .andExpect(model().attributeExists("emptyShippingList"))
                .andExpect(model().attributeExists("emptyPaymentList"))
                .andExpect(model().attributeExists("shippingAddress"))
                .andExpect(model().attributeExists("billingAddress"))
                .andExpect(model().attributeExists("payment"))
                .andExpect(model().attributeExists("cartItemList"))
                .andExpect(model().attributeExists("shoppingCart"))
                .andExpect(model().attributeExists("stateList"))
                .andExpect(model().attributeExists("classActiveShipping"))
                .andExpect(view().name("checkoutPage"));
    }

    @Test
    void checkoutWithIBookInStockAndUserShippingAndUserPaymentTest() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(userEntity).when(mockUserService).findUserByUsername("test");

        ShoppingCart mockSh = new ShoppingCart();
        mockSh.setId(1L);
        mockSh.setGrandTotal(BigDecimal.valueOf(1.0));

        CartItem mockCartItem = new CartItem();
        mockCartItem.setId(1L);
        mockCartItem.setQty(3);
        mockCartItem.setSubtotal(BigDecimal.valueOf(1.0));
        Book mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setInStockNumber(22);
        mockCartItem.setBook(mockBook);
        mockCartItem.setShoppingCart(mockSh);

        UserPayment mockUserPayment = getTestUserPayment();
        mockUserPayment.setUser(userEntity);
        userEntity.setUserPaymentList(List.of(mockUserPayment));

        UserShipping mockUserShipping = getTestUserShipping();
        mockUserShipping.setUser(userEntity);
        userEntity.setUserShippingList(List.of(mockUserShipping));



        mockSh.setCartItemList(List.of(mockCartItem));
        userEntity.setShoppingCart(mockSh);

        when(mockCartItemService.findByShoppingCart(userEntity.getShoppingCart())).thenReturn(List.of(mockCartItem));

        RequestBuilder requestBuilder = get("/checkout")
                .param("id", "1")
                .param("cartId", "1")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(model().attributeExists("userShippingList"))
                .andExpect(model().attributeExists("userPaymentList"))
                .andExpect(model().attributeExists("emptyShippingList"))
                .andExpect(model().attributeExists("emptyPaymentList"))
                .andExpect(model().attributeExists("shippingAddress"))
                .andExpect(model().attributeExists("billingAddress"))
                .andExpect(model().attributeExists("payment"))
                .andExpect(model().attributeExists("cartItemList"))
                .andExpect(model().attributeExists("shoppingCart"))
                .andExpect(model().attributeExists("stateList"))
                .andExpect(model().attributeExists("classActiveShipping"))
                .andExpect(view().name("checkoutPage"));
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

    private UserShipping getTestUserShipping() {
        UserShipping userShipping = new UserShipping();
        userShipping.setId(1L);
        userShipping.setUserShippingDefault(true);
        return userShipping;
    }

    private UserPayment getTestUserPayment() {
        UserPayment userPayment = new UserPayment();
        userPayment.setId(1L);
        userPayment.setDefaultPayment(true);
        return userPayment;
    }

    private ShippingAddress getTestShippingAddress() {
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setId(1L);
        shippingAddress.setShippingAddressName("Test");
        shippingAddress.setShippingAddressStreet1("Test");
        shippingAddress.setShippingAddressStreet2("Test");
        shippingAddress.setShippingAddressCity("Test");
        shippingAddress.setShippingAddressState("Test");
        shippingAddress.setShippingAddressCountry("Test");
        shippingAddress.setShippingAddressZipcode("Test");
        return shippingAddress;
    }

    private BillingAddress getTestBillingAddress() {
        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setId(1L);
        billingAddress.setBillingAddressName("Test");
        billingAddress.setBillingAddressStreet1("Test");
        billingAddress.setBillingAddressStreet2("Test");
        billingAddress.setBillingAddressCity("Test");
        billingAddress.setBillingAddressState("Test");
        billingAddress.setBillingAddressCountry("Test");
        billingAddress.setBillingAddressZipcode("Test");
        return billingAddress;
    }

    private  UserBilling getTestUserBilling() {
        UserBilling userBilling = new UserBilling();
        userBilling.setId(1L);
        userBilling.setUserBillingName("Test");
        userBilling.setUserBillingStreet1("Test");
        userBilling.setUserBillingStreet2("Test");
        userBilling.setUserBillingCity("Test");
        userBilling.setUserBillingState("Test");
        userBilling.setUserBillingCountry("Test");
        userBilling.setUserBillingZipcode("Test");
        return userBilling;
    }
}


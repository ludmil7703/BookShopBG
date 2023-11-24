package org.softuni.bookshopbg.controlller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.softuni.bookshopbg.model.entities.*;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.service.*;
import org.softuni.bookshopbg.utils.MailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MailConstructor mailConstructor;

    @Mock
    private Principal mockPrincipal;

    @Mock
    private  UserService mockUserService;
    @Mock
    private  UserDetailsService mockUserDetailsService;
    @Mock
    private  BookService mockBookService;

    @Mock
    private UserPaymentService mockUserPaymentService;

    @Mock
    private UserShippingService mockUserShippingService;
    @Mock
    private CartItemService mockCartItemService;

    @Mock
    private OrderService mockOrderService;

    @Mock
    private  CategoryService mockCategoryService;

    @Mock
    private UserEntity mockUser;


    private UserController userControllerTest;

    @BeforeEach
    void setUp() {
        userControllerTest = new UserController( mockUserService,
                mockUserDetailsService,
                mockBookService,
                mockUserPaymentService,
                mockUserShippingService,
                mockCartItemService,
                mockOrderService,
                mockCategoryService);
        mockMvc = MockMvcBuilders.standaloneSetup(userControllerTest).build();
    }

 @AfterEach
    void tearDown() {
        userControllerTest = null;
    }

    @Test
    void contextLoads() {
        assertNotNull(mockMvc);
    }


    @Test
    void testGetLoginPage() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/login");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myAccount"))
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(model().attributeExists("classActiveLogin"));
    }
    @Test
    void BookshelfTest() throws Exception {
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(mockUser).when(mockUserService).findUserByUsername("test");


        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/bookshelf")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("bookList"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(model().attributeExists("bookList"))
                .andExpect(model().attributeExists("activeAll"));
    }

    @Test
    void bookDetailTest() throws Exception {
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(mockUser).when(mockUserService).findUserByUsername("test");

        when(mockBookService.findBookById(1L)).thenReturn(new Book());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/bookDetail")
                .param("id", "1")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("bookDetails"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("qty"))
                .andExpect(model().attributeExists("qtyList"))
                .andExpect(model().attributeExists("categoryList"));
    }


    @Test
    void forgetPasswordTest() {

    }


    @Test
    void myProfileTest() throws Exception {
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(mockUser).when(mockUserService).findUserByUsername("test");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/myProfile")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myProfilePage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("userPaymentList"))
                .andExpect(model().attributeExists("userShippingList"))
                .andExpect(model().attributeExists("orderList"))
                .andExpect(model().attributeExists("listOfCreditCards"))
                .andExpect(model().attributeExists("listOfShippingAddresses"))
                .andExpect(model().attributeExists("classActiveEdit"))
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(model().attributeExists("userShipping"));
    }

    @Test
    void listOfCreditCards() throws Exception {
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(mockUser).when(mockUserService).findUserByUsername("test");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/listOfCreditCards")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myProfilePage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("userPaymentList"))
                .andExpect(model().attributeExists("userShippingList"))
                .andExpect(model().attributeExists("orderList"))
                .andExpect(model().attributeExists("listOfCreditCards"))
                .andExpect(model().attributeExists("listOfShippingAddresses"))
                .andExpect(model().attributeExists("classActiveBilling"))
                .andExpect(model().attributeExists("categoryList"));
    }

    @Test
    void listOfShippingAddresses() throws Exception {
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(mockUser).when(mockUserService).findUserByUsername("test");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/listOfShippingAddresses")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myProfilePage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("userPaymentList"))
                .andExpect(model().attributeExists("userShippingList"))
                .andExpect(model().attributeExists("orderList"))
                .andExpect(model().attributeExists("listOfCreditCards"))
                .andExpect(model().attributeExists("listOfShippingAddresses"))
                .andExpect(model().attributeExists("classActiveShipping"))
                .andExpect(model().attributeExists("categoryList"));
    }


    @Test
    void addNewCreditCard() throws Exception {
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(mockUser).when(mockUserService).findUserByUsername("test");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/addNewCreditCard")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myProfilePage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("addNewCreditCard"))
                .andExpect(model().attributeExists("classActiveBilling"))
                .andExpect(model().attributeExists("listOfShippingAddresses"))
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(model().attributeExists("userBilling"))
                .andExpect(model().attributeExists("userPayment"))
                .andExpect(model().attributeExists("stateList"))
                .andExpect(model().attributeExists("userPaymentList"))
                .andExpect(model().attributeExists("userShippingList"))
                .andExpect(model().attributeExists("orderList"));
    }


    @Test
    void addNewShippingAddress() throws Exception {
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(mockUser).when(mockUserService).findUserByUsername("test");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/addNewShippingAddress")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myProfilePage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("addNewShippingAddress"))
                .andExpect(model().attributeExists("classActiveShipping"))
                .andExpect(model().attributeExists("listOfCreditCards"))
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(model().attributeExists("userShipping"))
                .andExpect(model().attributeExists("stateList"))
                .andExpect(model().attributeExists("userPaymentList"))
                .andExpect(model().attributeExists("userShippingList"))
                .andExpect(model().attributeExists("orderList"));
    }

    @Test
    void testAddNewCreditCard() throws Exception {
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(mockUser).when(mockUserService).findUserByUsername("test");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/addNewCreditCard")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myProfilePage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("classActiveBilling"))
                .andExpect(model().attributeExists("listOfShippingAddresses"))
                .andExpect(model().attributeExists("listOfCreditCards"))
                .andExpect(model().attributeExists("userShippingList"))
                .andExpect(model().attributeExists("userPaymentList"))
                .andExpect(model().attributeExists("orderList"));
    }

    @Test
    void addNewShippingAddressPost() throws Exception {
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(mockUser).when(mockUserService).findUserByUsername("test");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/addNewShippingAddress")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myProfilePage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("classActiveShipping"))
                .andExpect(model().attributeExists("listOfCreditCards"))
                .andExpect(model().attributeExists("listOfShippingAddresses"))
                .andExpect(model().attributeExists("userShippingList"))
                .andExpect(model().attributeExists("userPaymentList"))
                .andExpect(model().attributeExists("orderList"));
    }

    @Test
    void updateCreditCard() throws Exception {
        when(mockPrincipal.getName()).thenReturn("test");
        UserPayment userPayment = new UserPayment();
        userPayment.setId(1L);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        userPayment.setUser(userEntity);
        userPayment.setUserBilling(new UserBilling());

        doReturn(userEntity).when(mockUserService).findUserByUsername("test");
        when(mockUserPaymentService.findById(1L)).thenReturn(userPayment);


        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/updateCreditCard")
                .param("id", "1")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myProfilePage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("userPayment"))
                .andExpect(model().attributeExists("userBilling"))
                .andExpect(model().attributeExists("userPaymentList"))
                .andExpect(model().attributeExists("userShippingList"))
                .andExpect(model().attributeExists("orderList"))
                .andExpect(model().attributeExists("stateList"))
                .andExpect(model().attributeExists("classActiveBilling"))
                .andExpect(model().attributeExists("listOfShippingAddresses"))
                .andExpect(model().attributeExists("classActiveBilling"))
                .andExpect(model().attributeExists("categoryList"));
    }

    @Test
    void testUpdateCreditCard() throws Exception {
        when(mockPrincipal.getName()).thenReturn("test");
        UserPayment userPayment = new UserPayment();
        userPayment.setId(1L);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(2L);
        doReturn(userEntity).when(mockUserService).findUserByUsername("test");

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(1L);
        userPayment.setUser(userEntity1);

        when(mockUserPaymentService.findById(1L)).thenReturn(userPayment);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/updateCreditCard")
                .param("id", "1")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    void updateUserShipping() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(userEntity).when(mockUserService).findUserByUsername("test");

        UserShipping userShipping = new UserShipping();
        userShipping.setId(1L);

        userShipping.setUser(userEntity);


        when(mockUserShippingService.findById(1L)).thenReturn(Optional.of(userShipping));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/updateUserShipping")
                .param("id", "1")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myProfilePage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("userShipping"))
                .andExpect(model().attributeExists("stateList"))
                .andExpect(model().attributeExists("addNewShippingAddress"))
                .andExpect(model().attributeExists("orderList"))
                .andExpect(model().attributeExists("listOfCreditCards"))
                .andExpect(model().attributeExists("classActiveShipping"))
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(model().attributeExists("userPaymentList"))
                .andExpect(model().attributeExists("userShippingList"));
    }

    @Test
    void testUpdateUserShipping() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(userEntity).when(mockUserService).findUserByUsername("test");

        UserShipping userShipping = new UserShipping();
        userShipping.setId(1L);

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(2L);
        userShipping.setUser(userEntity1);

        when(mockUserShippingService.findById(1L)).thenReturn(Optional.of(userShipping));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/updateUserShipping")
                .param("id", "1")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    void setDefaultPayment() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(userEntity).when(mockUserService).findUserByUsername("test");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/setDefaultPayment")
                .param("defaultUserPaymentId", "1")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myProfilePage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("userPaymentList"))
                .andExpect(model().attributeExists("userShippingList"))
                .andExpect(model().attributeExists("orderList"))
                .andExpect(model().attributeExists("listOfCreditCards"))
                .andExpect(model().attributeExists("listOfShippingAddresses"))
                .andExpect(model().attributeExists("classActiveBilling"));
    }


    @Test
    void setDefaultShippingAddress() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(userEntity).when(mockUserService).findUserByUsername("test");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/setDefaultShippingAddress")
                .param("defaultShippingAddressId", "1")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myProfilePage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("userPaymentList"))
                .andExpect(model().attributeExists("userShippingList"))
                .andExpect(model().attributeExists("orderList"))
                .andExpect(model().attributeExists("listOfCreditCards"))
                .andExpect(model().attributeExists("listOfShippingAddresses"))
                .andExpect(model().attributeExists("classActiveShipping"));
    }

    @Test
    void removeCreditCard() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(userEntity).when(mockUserService).findUserByUsername("test");
        UserPayment userPayment = new UserPayment();
        userPayment.setId(1L);
        userPayment.setUser(userEntity);

        when(mockUserPaymentService.findById(1L)).thenReturn(userPayment);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/removeCreditCard")
                .param("id", "1")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myProfilePage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("userPaymentList"))
                .andExpect(model().attributeExists("userShippingList"))
                .andExpect(model().attributeExists("orderList"))
                .andExpect(model().attributeExists("listOfCreditCards"))
                .andExpect(model().attributeExists("listOfShippingAddresses"))
                .andExpect(model().attributeExists("classActiveBilling"))
                .andExpect(model().attributeExists("categoryList"));
    }

    @Test
    void  removeCreditCardWithDifferentUsersTest() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(userEntity).when(mockUserService).findUserByUsername("test");
        UserPayment userPayment = new UserPayment();
        userPayment.setId(1L);

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(2L);
        userPayment.setUser(userEntity1);

        when(mockUserPaymentService.findById(1L)).thenReturn(userPayment);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/removeCreditCard")
                .param("id", "1")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    void removeUserShipping() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(userEntity).when(mockUserService).findUserByUsername("test");

        UserShipping userShipping = new UserShipping();
        userShipping.setId(1L);
        userShipping.setUser(userEntity);

        when(mockUserShippingService.findById(1L)).thenReturn(Optional.of(userShipping));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/removeUserShipping")
                .param("id", "1")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myProfilePage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("userPaymentList"))
                .andExpect(model().attributeExists("userShippingList"))
                .andExpect(model().attributeExists("orderList"))
                .andExpect(model().attributeExists("listOfShippingAddresses"))
                .andExpect(model().attributeExists("classActiveShipping"))
                .andExpect(model().attributeExists("categoryList"));
    }

    @Test
    void removeUserShippingWithDifferentUsersTest() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(userEntity).when(mockUserService).findUserByUsername("test");

        UserShipping userShipping = new UserShipping();
        userShipping.setId(1L);

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(2L);
        userShipping.setUser(userEntity1);

        when(mockUserShippingService.findById(1L)).thenReturn(Optional.of(userShipping));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/removeUserShipping")
                .param("id", "1")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    void newUserPost() {
    }

    @Test
    void newUser() {

    }


    @Test
    void updateUserInfo() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test");
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(userEntity).when(mockUserService).findUserByUsername(userEntity.getUsername());

        doReturn(userEntity).when(mockUserService).findUserByEmail(any());
        UserDetails userDetails = mock(UserDetails.class);
        when(mockUserDetailsService.loadUserByUsername(userEntity.getUsername())).thenReturn(userDetails);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/updateUserInfo")
                .param("email", "test")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myProfilePage"));
    }

    @Test
    void testUpdateUserInfoEmailExist() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test");
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(userEntity).when(mockUserService).findUserByUsername(userEntity.getUsername());

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(2L);
        userEntity1.setEmail("test1");
        doReturn(userEntity1).when(mockUserService).findUserByEmail(any());
        UserDetails userDetails = mock(UserDetails.class);
        when(mockUserDetailsService.loadUserByUsername(userEntity.getUsername())).thenReturn(userDetails);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/updateUserInfo")
                .param("email", "test")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myProfilePage"))
                .andExpect(model().attributeExists("emailExists"));
    }

    @Test
    void updateInfoUsernameExists() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("test");
        userEntity.setEmail("test");
        userEntity.setPassword("test");
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(userEntity).when(mockUserService).findUserByUsername(userEntity.getUsername());

        doReturn(null).when(mockUserService).findUserByEmail(any());
        UserDetails userDetails = mock(UserDetails.class);
        when(mockUserDetailsService.loadUserByUsername(userEntity.getUsername())).thenReturn(userDetails);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/updateUserInfo")
                .param("username", "test")
                .param("id", "2")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myProfilePage"))
                .andExpect(model().attributeExists("usernameExists"));
    }


    @Test
    void orderDetail() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(userEntity).when(mockUserService).findUserByUsername("test");

        Order order = new Order();
        order.setId(1L);
        order.setUser(userEntity);
        when(mockOrderService.findById(1L)).thenReturn(Optional.of(order));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/orderDetail")
                .param("id", "1")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("myProfilePage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("userPaymentList"))
                .andExpect(model().attributeExists("userShippingList"))
                .andExpect(model().attributeExists("orderList"))
                .andExpect(model().attributeExists("userShipping"))
                .andExpect(model().attributeExists("stateList"))
                .andExpect(model().attributeExists("listOfCreditCards"))
                .andExpect(model().attributeExists("listOfShippingAddresses"))
                .andExpect(model().attributeExists("classActiveOrders"))
                .andExpect(model().attributeExists("displayOrderDetail"))
                .andExpect(model().attributeExists("categoryList"));
    }

    @Test
    void testOrderDetailWithDifferentUsers() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(userEntity).when(mockUserService).findUserByUsername("test");
        Order order = new Order();
        order.setId(1L);

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(2L);
        order.setUser(userEntity1);

        when(mockOrderService.findById(1L)).thenReturn(Optional.of(order));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/orderDetail")
                .param("id", "1")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    void testRegister() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/register");
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(view().name("register"));
        resultActions.andExpect(model().attributeExists("userRegisterBindingModel"));

    }

//    @Test
//    void testRegisterSubmit() throws Exception {
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/register");
//        ResultActions resultActions = mockMvc.perform(requestBuilder);
//
//        mockUserService.register(new UserRegisterBindingModel());
//        resultActions.andExpect(status().isOk());
//
//            }

    @Test
    void addAttribute() {

    }





    private  List<Category> createCategoryList() {
        Category category = new Category();
        category.setCategoryName(CategoryName.ENGINEERING);
        category.setId(1L);
        return List.of(category);
    }
}
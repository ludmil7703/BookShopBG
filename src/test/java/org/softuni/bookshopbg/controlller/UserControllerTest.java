package org.softuni.bookshopbg.controlller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.service.*;
import org.softuni.bookshopbg.utils.MailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
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
    void myProfileTest() {

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


    @Test
    void listOfCreditCards() {
    }

    @Test
    void listOfShippingAddresses() {
    }

    @Test
    void addNewCreditCard() {
    }

    @Test
    void addNewShippingAddress() {
    }

    @Test
    void testAddNewCreditCard() {
    }

    @Test
    void addNewShippingAddressPost() {
    }

    @Test
    void updateCreditCard() {
    }

    @Test
    void updateUserShipping() {
    }

    @Test
    void setDefaultPayment() {
    }

    @Test
    void setDefaultShippingAddress() {
    }

    @Test
    void removeCreditCard() {
    }

    @Test
    void removeUserShipping() {
    }

    @Test
    void newUserPost() {
    }

    @Test
    void newUser() {
    }

    @Test
    void updateUserInfo() {
    }

    @Test
    void orderDetail() {
    }

    private  List<Category> createCategoryList() {
        Category category = new Category();
        category.setCategoryName(CategoryName.ENGINEERING);
        category.setId(1L);
        return List.of(category);
    }
}
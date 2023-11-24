package org.softuni.bookshopbg.controlller;

import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.bookshopbg.model.dto.UserRegisterBindingModel;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.service.*;
import org.softuni.bookshopbg.utils.MailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

    @Autowired
    private  UserService mockUserService;
    @Autowired
    private  UserDetailsService mockUserDetailsService;
    @Autowired
    private  BookService mockBookService;

    @Autowired
    private UserPaymentService mockUserPaymentService;

    @Autowired
    private UserShippingService mockUserShippingService;
    @Autowired
    private CartItemService mockCartItemService;

    @Autowired
    private OrderService mockOrderService;

    @Autowired
    private  CategoryService mockCategoryService;

    @Mock
    private UserEntity mockUser;


    private UserController userControllerTest;

    @BeforeEach
    void setUp() {
        userControllerTest = new UserController( mockUserService,  mockUserDetailsService,  mockBookService,  mockUserPaymentService,  mockUserShippingService,  mockCartItemService,  mockOrderService,  mockCategoryService);
        mockMvc = MockMvcBuilders.standaloneSetup(userControllerTest).build();
    }

    @After
    void tearDown() {
        userControllerTest = null;
    }

    @Test
    void contextLoads() {
        assertNotNull(mockMvc);
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
    void testGetLoginPage() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/login");
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(view().name("login"));





    }

    @Test
    void testBookshelf() throws Exception {


        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/bookshelf")
                .principal(mockPrincipal);

        // Arrange
        doReturn(mockUser.getUsername()).when(mockPrincipal).getName();
        doThrow(new IllegalArgumentException()).when(mockUserService).findUserByUsername(mockPrincipal.getName());

        doReturn(mockUser).when(mockUserService).findUserByUsername(mockPrincipal.getName());
        List<Book> bookList = Collections.emptyList();
        List<Category> categoryList = Collections.emptyList();



        when(mockBookService.findAllBooks()).thenReturn(bookList);
        when(mockCategoryService.getAllCategories()).thenReturn(categoryList);


        // Act
        String result = userControllerTest.bookshelf((Model) model(), mockPrincipal);

        // Assert

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("bookList"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(model().attributeExists("bookList"))
                .andExpect(model().attributeExists("activeAll"));

    }

}
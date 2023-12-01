package org.softuni.bookshopbg.controlller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.service.BookService;
import org.softuni.bookshopbg.service.UserService;
import org.softuni.bookshopbg.service.impl.BookServiceImpl;
import org.softuni.bookshopbg.service.impl.CategoryServiceImpl;
import org.softuni.bookshopbg.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @Mock
    private BookServiceImpl mockBookService;

    @Mock
    private CategoryServiceImpl mockCategoryService;

    @Mock
    private UserServiceImpl mockUserService;
    @Mock
    private Principal mockPrincipal;
    @Mock
    private BookBindingModel mockBookDTO;

    @Mock
    private Category mockCategory;

    @Mock
    private UserEntity mockUser;

    @Mock
    private Book mockBook;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new HomeController(mockCategoryService, mockBookService, mockUserService))
                .build();
        mockBookDTO = new BookBindingModel();
        mockBookDTO.setId(1L);
        mockBookDTO.setAuthor("test");

        mockCategory = new Category();
        mockCategory.setCategoryName(CategoryName.IT);

        mockUser = new UserEntity();
        mockUser.setUsername("test123");

        mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setAuthor("test");
    }


    @Test
    void testHomePage() throws Exception {

        when(mockBookService.findAll()).thenReturn(List.of(mockBookDTO));
        when(mockCategoryService.getAllCategories()).thenReturn(List.of(mockCategory));
        RequestBuilder request = get("/");

        mockMvc.perform(request)
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("bookShelf"))
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(status().isOk());
    }

    @Test
    void testContactPage() throws Exception {

        when(mockCategoryService.getAllCategories()).thenReturn(List.of(mockCategory));

        RequestBuilder request = get("/contact");

        mockMvc.perform(request)
                .andExpect(view().name("contactInfo"))
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(status().isOk());

    }

    @Test
    void testSubpage() throws Exception {

        when(mockCategoryService.getAllCategories()).thenReturn(List.of(mockCategory));
        RequestBuilder request = get("/subpage");

        mockMvc.perform(request)
                .andExpect(view().name("subpageInfo"))
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(status().isOk());

    }
    @Test
    @WithMockUser(username = "test123", password = "1234", roles = {"USER","ADMIN"})
    void testAdminPage() throws Exception {
        RequestBuilder request = get("/admin");

        mockMvc.perform(request)
                .andExpect(view().name("adminPage"))
                .andExpect(status().isOk());
    }

    @Test
    void testBookDetailsWithLoginUser() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("test");
        userEntity.setEmail("test");
        userEntity.setPassword("test");
        when(mockPrincipal.getName()).thenReturn("test");
        doReturn(userEntity).when(mockUserService).findUserByUsername(userEntity.getUsername());

        BookBindingModel bookBindingModel = new BookBindingModel();
        Book book = new Book();
        when(mockBookService.findBookById(1L)).thenReturn(book);
        doReturn(bookBindingModel).when(mockBookService).mapBookToBookBindingModel(book);

        mockMvc.perform(get("/bookDetail/{id}", 1L)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("bookDetails"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("qty"))
                .andExpect(model().attributeExists("qtyList"));

    }

    @Test
    void testFaqPage() throws Exception {
        Category category = new Category();
        category.setCategoryName(CategoryName.IT);

        when(mockCategoryService.getAllCategories()).thenReturn(List.of(category));

        RequestBuilder request = get("/faq");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("faqInfo"))
                .andExpect(model().attributeExists("categoryList"));
    }
}
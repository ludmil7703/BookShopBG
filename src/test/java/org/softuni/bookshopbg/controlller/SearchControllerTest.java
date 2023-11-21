package org.softuni.bookshopbg.controlller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.service.impl.BookServiceImpl;
import org.softuni.bookshopbg.service.impl.CategoryServiceImpl;
import org.softuni.bookshopbg.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookServiceImpl mockBookService;

    @Mock
    private CategoryServiceImpl mockCategoryService;

    @Mock
    private UserServiceImpl mockUserService;

    @Mock
    private Category mockCategory;

    @Mock
    private Book mockBook;

    @Mock
    private Principal mockPrincipal;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new SearchController(mockUserService, mockBookService, mockCategoryService))
                .build();

        mockPrincipal = new Principal() {
            @Override
            public String getName() {
                return "test";
            }
        };

        mockCategory = new Category();
        mockCategory.setCategoryName(CategoryName.IT);

        mockBook = new Book();
        mockBook.setAuthor("test");
        mockBook.setCategory(mockCategory);

        when(mockBookService.findByCategory(CategoryName.IT))
                .thenReturn(List.of(mockBook));

        when(mockBookService.blurrySearch("test"))
                .thenReturn(List.of(mockBook));

        when(mockCategoryService.getAllCategories())
                .thenReturn(List.of(mockCategory));
    }



    @Test
    void searchTest() throws Exception {
        RequestBuilder request = get("/search");

        mockMvc.perform(request)
                .andExpect(view().name("bookShelf"))
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(model().attributeExists("bookShelf"))
                .andExpect(status().isOk());
    }

    @Test
    void searchByCategoryTest() throws Exception {

        RequestBuilder request = get("/searchByCategory")
                .param("category", CategoryName.IT.name())
                .principal(mockPrincipal);

        mockMvc.perform(request)
                .andExpect(view().name("bookShelf"))
                .andExpect(model().attributeExists("bookShelf"))
                .andExpect(status().isOk());
    }

    @Test
    void searchByCategoryWithNonExistingCategoryTest() throws Exception {
        when(mockBookService.findByCategory(CategoryName.IT))
                .thenReturn(List.of());

        RequestBuilder request = get("/searchByCategory")
                .param("category", CategoryName.IT.name())
                .principal(mockPrincipal);

        mockMvc.perform(request)
                .andExpect(view().name("bookShelf"))
                .andExpect(model().attributeExists("emptyList"))
                .andExpect(model().attributeExists("bookShelf"))
                .andExpect(status().isOk());
    }


    @Test
    void searchBookWithExistingBookTest() throws Exception {


        RequestBuilder request = get("/searchBook")
                .param("keyword", "test");

        mockMvc.perform(request)
                .andExpect(view().name("bookShelf"))
                .andExpect(model().attributeExists("bookShelf"))
                .andExpect(status().isOk());
    }

    @Test
    void searchBookWithNonExistingBookTest() throws Exception {
        when(mockBookService.blurrySearch("test"))
                .thenReturn(List.of());

        RequestBuilder request = get("/searchBook")
                .param("keyword", "test");

        mockMvc.perform(request)
                .andExpect(view().name("bookShelf"))
                .andExpect(model().attributeExists("emptyList"))
                .andExpect(model().attributeExists("bookShelf"))
                .andExpect(status().isOk());
    }
}
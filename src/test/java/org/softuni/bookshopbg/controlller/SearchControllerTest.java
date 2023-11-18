package org.softuni.bookshopbg.controlller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.service.impl.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;



    @Test
    void searchTest() throws Exception {
        RequestBuilder request = get("/search");

        mockMvc.perform(request)
                .andExpect(view().name("bookshelf"))
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(model().attributeExists("bookShelf"))
                .andExpect(status().isOk());
    }

    @Test
    void searchByCategoryTest() throws Exception {
        RequestBuilder request = get("/searchByCategory")
                .param("category", CategoryName.IT.name());

        mockMvc.perform(request)
                .andExpect(view().name("bookshelf"))
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(model().attributeExists("bookShelf"))
                .andExpect(status().isOk());
    }


    @Test
    void searchBook() throws Exception {
        RequestBuilder request = get("/searchBook")
                .param("search", "test");

        mockMvc.perform(request)
                .andExpect(view().name("bookshelf"))
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(model().attributeExists("bookShelf"))
                .andExpect(status().isOk());
    }
}
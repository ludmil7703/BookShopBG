package org.softuni.bookshopbg.controlller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.softuni.bookshopbg.service.BookService;
import org.softuni.bookshopbg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @Mock
    private BookService mockBookService;

    @Mock
    private UserService mockUserService;
    @Mock
    private Principal mockPrincipal;


    @Test
    void testHomePage() throws Exception {
        RequestBuilder request = get("/");

        mockMvc.perform(request)
                .andExpect(view().name("/index"))
                .andExpect(model().attributeExists("bookShelf"))
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(status().isOk());
    }

    @Test
    void testContactPage() throws Exception {
        RequestBuilder request = get("/contact");

        mockMvc.perform(request)
                .andExpect(view().name("contact"))
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(status().isOk());

    }

    @Test
    void testSubpage() throws Exception {
        RequestBuilder request = get("/subpage");

        mockMvc.perform(request)
                .andExpect(view().name("subpage"))
                .andExpect(status().isOk());

    }
    @Test
    @WithMockUser(username = "test123", password = "1234", roles = {"USER","ADMIN"})
    void testAdminPage() throws Exception {
        RequestBuilder request = get("/admin");

        mockMvc.perform(request)
                .andExpect(view().name("admin"))
                .andExpect(status().isOk());
    }

    @Test
    void testBookDetails() {

    }

    @Test
    void testFaqPage() throws Exception {
        RequestBuilder request = get("/faq");

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }
}
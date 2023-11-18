package org.softuni.bookshopbg.controlller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.bookshopbg.exception.ObjectNotFoundException;
import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.repositories.BookRepository;
import org.softuni.bookshopbg.service.BookService;
import org.softuni.bookshopbg.service.impl.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RestBookControllerTest {

    @InjectMocks
    private RestBookController restBookController;

    @Mock
    private BookService mockBookService;

    @Mock
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        this.restBookController = new RestBookController(mockBookService);
    }

    @AfterEach
    void tearDown() {
        this.restBookController = null;
    }

    @Test
    void getAllBooks() throws Exception {
        BookBindingModel book = new BookBindingModel();
        book.setAuthor("Author");
        book.setDescription("Description");

        List<BookBindingModel> books = List.of(book);

        when(mockBookService.findAll()).thenReturn(books);

        restBookController.getAllBooks();

        assertEquals(1, books.size());

    }
//
//    @Test
//    void deleteBookById() throws Exception {
//        long id = 1L;
//
//        String requestURI = "/api/books/" + id;
//
//        doNothing().when(mockBookService).deleteBookById(id);
//
//        mockMvc.perform(delete(requestURI))
//                .andExpect(status().isNoContent())
//                .andDo(print());
//    }
}
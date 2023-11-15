package org.softuni.bookshopbg.controlller;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.softuni.bookshopbg.exception.ObjectNotFoundException;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.repositories.BookRepository;
import org.softuni.bookshopbg.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test123", password = "1234", roles = {"USER","ADMIN"})
class AdminControllerTest {



    @Autowired
    private MockMvc mockMvc;


    @Mock
    private BookService mockBookService;

    @Mock
    private BookRepository mockBookRepository;



    @Test
    void testGetAddBook() throws Exception {

        RequestBuilder request = get("/books/add");

        mockMvc.perform(request)
                .andExpect(view().name("addBook"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("categoryList"))
                .andExpect(status().isOk());
    }
    @Test
    void testGetAddBookWithWrongId() {
        when(mockBookService.findBookById(123L)).thenThrow(ObjectNotFoundException.class);

    }


    @Test
    void testAddBookPost() throws Exception {
        RequestBuilder request = post("/books/add")
                .param("title", "Title1")
                .param("author", "Author1")
                .param("description", "Description1")
                .param("releaseDate", "2020-01-01")
                .param("listPrice", "10")
                .param("ourPrice", "10")
                .param("numberOfPages", "10")
                .param("shippingWeight", "10")
                .param("format", "format")
                .param("isbn", "10")
                .param("inStockNumber", "10")
                .param("category", "IT")
                .param("id", "1")
                .param("language", "language")
                .param("publisher", "publisher");


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books/bookList"));
    }

    @Test
    void testAddBookPostWithWrongData() throws Exception {
        RequestBuilder requestBuilder = post("/books/add")
                .param("title", "Title1");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books/add"));

    }

    @Test
    void testGetBookInfo() throws Exception {
        RequestBuilder request = get("/books/bookInfo/{id}", 1L);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("bookInfo"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void testGetUpdateBook() throws Exception {
        RequestBuilder request = get("/books/updateBook/{id}", 1L);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("updateBook"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("categoryList"));
    }

    @Test
    void testGetUpdateBookWithWrongId() {
        when(mockBookService.findBookById(123L)).thenThrow(ObjectNotFoundException.class);
    }

    @Test
    void testUpdateBook() throws Exception {

        RequestBuilder request = post("/books/updateBook")
                .param("title", "Title1")
                .param("author", "Author1")
                .param("description", "Description1")
                .param("releaseDate", "2020-01-01")
                .param("listPrice", "10")
                .param("ourPrice", "10")
                .param("numberOfPages", "10")
                .param("shippingWeight", "10")
                .param("format", "format")
                .param("isbn", "10")
                .param("inStockNumber", "10")
                .param("category", "IT")
                .param("id", "1")
                .param("language", "language")
                .param("publisher", "publisher");


      mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books/bookInfo/" + getBook().getId()));


    }

    @Test
    void testUpdateBookWithWrongData() throws Exception {
        RequestBuilder requestBuilder = post("/books/updateBook")
                .param("title", "Title1");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books/add"));

    }

    @Test
    void testGetBookList() throws Exception {

        RequestBuilder request = get("/books/bookList");
                mockMvc.perform(request)
                        .andExpect(status().isOk())
                        .andExpect(view().name("bookList"))
                        .andExpect(model().attributeExists("bookList"));
    }

    @Test
    void testRemove() throws Exception {

        RequestBuilder request = delete("/books/remove/{id}",1L);

        when(mockBookService.deleteBookById(1L)).thenReturn(getBook());
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books/bookList"));
    }

    @Test
void testRemoveWithWrongId() {
    when(mockBookService.deleteBookById(123L)).thenThrow(ObjectNotFoundException.class);
}

    public Book getBook() {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Author");
        book.setCategory(new Category(CategoryName.IT));
        book.setDescription("Description");
        book.setListPrice(BigDecimal.valueOf(10));
        book.setReleaseDate(new Date());
        book.setTitle("Title");
        book.setNumberOfPages(10);
        book.setInStockNumber(10);
        return book;
    }

}
package org.softuni.bookshopbg.controlller;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.softuni.bookshopbg.exception.ObjectNotFoundException;
import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.service.BookService;
import org.softuni.bookshopbg.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
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
    private CategoryService mockCategoryService;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AdminController(mockBookService, mockCategoryService)).build();
        Mockito.reset(mockBookService);
        Mockito.reset(mockCategoryService);
    }


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
       Book book = getBook();
        when(mockBookService.findBookById(book.getId())).thenReturn(book);
        RequestBuilder request = MockMvcRequestBuilders.get("/books/bookInfo/1");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("bookInfo"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void testGetUpdateBook() throws Exception {
        Book book = getBook();
        Category category = new Category(CategoryName.IT);
        List<Category> categoryList = List.of(category);
        BookBindingModel bookBindingModel = getBookBindingModel();
        when(mockBookService.mapBookToBookBindingModel(book)).thenReturn(bookBindingModel);
        when(mockBookService.findBookById(1L)).thenReturn(book);
        when(mockCategoryService.getAllCategories()).thenReturn(categoryList);
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
//Must return a bookInfo with id 1

        RequestBuilder requestBuilder = post("/books/updateBook")
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

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books/bookInfo/1"));

    }

    @Test
    void testUpdateBookWithWrongData() throws Exception {
        RequestBuilder requestBuilder= post("/books/updateBook")
                .param("title", "Title1")
                .param("inStockNumber", "abc")
                .param("id", "1");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books/updateBook/1" ));

    }

    @Test
    void testGetBookList() throws Exception {
        BookBindingModel book = getBookBindingModel();
        List<BookBindingModel> bookList = List.of(book);
        when(mockBookService.findAll()).thenReturn(bookList);
        RequestBuilder request = get("/books/bookList");
                mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("bookShelfAdmin"))
                .andExpect(model().attributeExists("bookShelf"));
    }

    @Test
    void testRemove() throws Exception {
        Book book = getBook();
        when(mockBookService.deleteBookById(1L)).thenReturn(book);
        BookBindingModel bookBindingModel = getBookBindingModel();

        List<BookBindingModel> bookBindingModelList = List.of(bookBindingModel);
        when(mockBookService.findAll()).thenReturn(bookBindingModelList);
RequestBuilder request = delete("/books/remove/{id}", 1);

        ResultActions resultActions = mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books/bookList"));

        resultActions.andExpect(redirectedUrl("/books/bookList"));
    }


    @Test
void testRemoveWithWrongId() throws Exception {
        assertThrows(ServletException.class, () -> {
            mockMvc.perform(delete("/books/remove/{id}", 123L));
        });
}

    private Book getBook() {
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

    private BookBindingModel getBookBindingModel() {
        BookBindingModel bookBindingModel = new BookBindingModel();
        bookBindingModel.setAuthor("Author");
        bookBindingModel.setId(1L);
        bookBindingModel.setCategory(CategoryName.ENGINEERING);
        bookBindingModel.setDescription("Description");
        bookBindingModel.setListPrice(BigDecimal.valueOf(10));
        bookBindingModel.setReleaseDate(new Date());
        bookBindingModel.setTitle("Title");
        bookBindingModel.setNumberOfPages(10);
        bookBindingModel.setInStockNumber(10);
        return bookBindingModel;
    }

}
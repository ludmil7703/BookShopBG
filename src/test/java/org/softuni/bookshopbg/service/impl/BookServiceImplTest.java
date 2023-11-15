package org.softuni.bookshopbg.service.impl;

import lombok.With;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.repositories.BookRepository;
import org.softuni.bookshopbg.repositories.CategoryRepository;
import org.softuni.bookshopbg.service.BookService;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository mockBookRepository;

    @Mock
    private CategoryRepository mockCategoryRepository;

    private BookService bookServiceToTest;

    @BeforeEach
    void setUp() {
        bookServiceToTest = new BookServiceImpl(mockBookRepository, mockCategoryRepository, new ModelMapper());
    }


    @Test
    void testSaveBook() throws IOException {
        Book book = createBook();

        when(mockBookRepository.save(book))
                .thenReturn(book);

        Book resultBook = bookServiceToTest.saveBook(book);

        assertEquals(book, resultBook);
        verify(mockBookRepository, times(1)).save(book);

    }


    @Test
    void testFindBookById() {
        Book book = createBook();

        when(mockBookRepository.findById(1L))
                .thenReturn(java.util.Optional.of(book));

        Optional<BookBindingModel> resultBook = bookServiceToTest.findBookById(1L);

        assertEquals(book.getAuthor(), resultBook.get().getAuthor());
        assertEquals(book.getCategory().getCategoryName(), resultBook.get().getCategory());
    }

    @Test
    void testDeleteBookById() {
        Book resultBook = createBook();

        when(mockBookRepository.deleteBookById(1L)
                ).thenReturn(resultBook);

        Book expectedBook = bookServiceToTest.deleteBookById(1L);

        assertEquals(expectedBook, resultBook);
        assertEquals(expectedBook.getAuthor(), resultBook.getAuthor());

    }

    @Test
    void testDeleteBookWithException(){


        when(mockBookRepository.deleteBookById(1L)
                ).thenReturn(null);


        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookServiceToTest.deleteBookById(1L);
        });


        String expectedMessage = "Book with the given id was not found!";

       assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testFindAll() throws IOException {

        Book book = createBook();

        when(mockBookRepository.findAll())
                .thenReturn(List.of(book));

        List<BookBindingModel> books = bookServiceToTest.findAll();

        assertEquals(1, books.size(), "Books count is not correct!");
        assertEquals("Author", books.get(0).getAuthor(), "Author is not correct!");
    }

    @Test
    void testMapBookToBookBindingModel() {
        Book book = createBook();
        BookBindingModel bookBindingModel = bookServiceToTest.mapBookToBookBindingModel(book);

        assertEquals(book.getAuthor(), bookBindingModel.getAuthor(), "Author is not correct!");
        assertEquals(book.getCategory().getCategoryName(), bookBindingModel.getCategory(), "Category is not correct!");
        assertEquals(book.getDescription(), bookBindingModel.getDescription(), "Description is not correct!");
        assertEquals(book.getTitle(), bookBindingModel.getTitle(), "Title is not correct!");

    }

    @Test
    void testBlurrySearch() {
        Book book = createBook();
        when(mockBookRepository.findByTitleContaining("T"))
                .thenReturn(List.of(book));

        List<Book> books = bookServiceToTest.blurrySearch("T");

        assertEquals(1, books.size(), "Books count is not correct!");
        assertEquals("Author", books.get(0).getAuthor(), "Author is not correct!");
            }

    @Test
    void testFindByCategory() {
        Book book = createBook();
        when(mockBookRepository.findAllByCategoryCategoryName(CategoryName.ENGINEERING))
                .thenReturn(List.of(book));

        List<Book> books = bookServiceToTest.findByCategory(CategoryName.ENGINEERING);

        assertEquals(1, books.size(), "Books count is not correct!");
        assertEquals("Author", books.get(0).getAuthor(), "Author is not correct!");
    }

    private Book createBook() {
        Book book = new Book();
        book.setAuthor("Author");
        book.setLanguage("Language");
        book.setNumberOfPages(100);
        book.setInStockNumber(10);
        book.setReleaseDate(new Date());
        book.setCategory(new Category(CategoryName.ENGINEERING));
        book.setListPrice(BigDecimal.TEN);
        book.setDescription("Description");
        book.setImageUrl("Image URL");
        book.setTitle("Title");
        book.setActive(true);
        return book;
    }

}
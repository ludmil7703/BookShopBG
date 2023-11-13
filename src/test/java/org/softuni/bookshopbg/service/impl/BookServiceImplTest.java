package org.softuni.bookshopbg.service.impl;

import org.junit.jupiter.api.AfterEach;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository mockBookRepository;

    @Mock
    private CategoryRepository mockCategoryRepository;

    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        mockBookRepository.deleteAll();
        mockCategoryRepository.deleteAll();

        Category category = new Category();
        category.setCategoryName(CategoryName.ENGINEERING);
        Book book1 = new Book();
        book1.setAuthor("Author1");
        book1.setLanguage("Language1");
        book1.setNumberOfPages(100);
        book1.setInStockNumber(10);


        Book book2 = new Book();
        book2.setAuthor("Author2");
        book2.setLanguage("Language2");
        book2.setNumberOfPages(200);
        book2.setInStockNumber(20);
    }

    @AfterEach
    void tearDown() {
        mockBookRepository.deleteAll();
        mockCategoryRepository.deleteAll();
    }


    @Test
    void findBookById() {
        Book book = createBook();
        when(mockBookRepository.findById(1L))
                .thenReturn(java.util.Optional.of(book));

        Book book1 = mockBookRepository.findById(1L).orElse(null);
        assertNotNull(book1, "Book is null!");
        assertEquals("Author", book1.getAuthor(), "Author is not correct!");
    }

    @Test
    void deleteBookById() {
        Book book = createBook();
        when(mockBookRepository.findById(1L))
                .thenReturn(java.util.Optional.of(book));

        mockBookRepository.deleteById(1L);
        Book book1 = mockBookRepository.findById(1L).orElse(null);
        assertNull(book1, "Book is not null!");
    }

    @Test
    void testGetAllBooks() {

        Book book = createBook();
        when(mockBookRepository.findAll())
                .thenReturn(List.of(book));

        List<Book> books = mockBookRepository.findAll();

        assertEquals(1, books.size(), "Books count is not correct!");
        assertEquals("Author", books.get(0).getAuthor(), "Author is not correct!");
    }

    @Test
    void mapBookToBookBindingModel() {

    }

    @Test
    void testBlurrySearch() {
        Book book = createBook();
        when(mockBookRepository.findByTitleContaining("T"))
                .thenReturn(List.of(book));

        List<Book> books = mockBookRepository.findByTitleContaining("T");

        assertEquals(1, books.size(), "Books count is not correct!");
        assertEquals("Author", books.get(0).getAuthor(), "Author is not correct!");
            }

    @Test
    void findByCategory() {
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
        return book;
    }

}
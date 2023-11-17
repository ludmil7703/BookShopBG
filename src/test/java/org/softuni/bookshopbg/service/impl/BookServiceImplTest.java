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
import org.softuni.bookshopbg.service.BookService;
import org.softuni.bookshopbg.utils.CloudinaryConfig;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository mockBookRepository;

    @Mock
    private CategoryRepository mockCategoryRepository;
    @Mock
    private CloudinaryConfig mockCloudinary;

    @Mock
    private MultipartFile mockMultipartFile;

    private BookService bookServiceToTest;

    @BeforeEach
    void setUp() {
        bookServiceToTest = new BookServiceImpl(mockBookRepository, mockCategoryRepository, new ModelMapper(), mockCloudinary);
    }

    @AfterEach
    void tearDown() {
        bookServiceToTest = null;
    }



    @Test
    void testSave() {
        Book book = createBook();

        when(mockBookRepository.save(book)).thenReturn(book);

        when(mockBookRepository.save(book))
                .thenReturn(book);

        when(mockCategoryRepository.findCategoryByCategoryName(book.getCategory().getCategoryName()))
                .thenReturn(new Category(CategoryName.ENGINEERING));

        Category category = mockCategoryRepository.findCategoryByCategoryName(book.getCategory().getCategoryName());

        book.setCategory(category);

        when(mockBookRepository.save(book))
                .thenReturn(book);

        Book resultBook = mockBookRepository.save(book);

        verify(mockBookRepository, times(1)).save(book);

        assertEquals(book.getCategory(), resultBook.getCategory());

        verify(mockBookRepository, times(1)).save(book);
    }

    @Test
    void testSaveBookWithException() throws IOException {
        Book book = createBook();

        when(mockBookRepository.save(book))
                .thenReturn(null);

        bookServiceToTest.saveBook(book);
      assertThrows(IllegalArgumentException.class, () -> {
            bookServiceToTest.saveBook(null);
        });
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

        when(mockBookRepository.findBookById(1L))
                .thenReturn(book);

        Book resultBook = bookServiceToTest.findBookById(1L);

        assertEquals(book.getAuthor(), resultBook.getAuthor());
        assertEquals(book.getCategory().getCategoryName(), resultBook.getCategory().getCategoryName());
    }

    @Test
    void testDeleteBookById() {
        Book resultBook = createBook();

        when(mockBookRepository.deleteBookById(1L))
                .thenReturn(resultBook);

        Book book = mockBookRepository.deleteBookById(1L);

        assertEquals(book, resultBook);
        assertEquals(book.getAuthor(), resultBook.getAuthor());

    }

    @Test
    void testDeleteBookWithException(){
        when(mockBookRepository.findBookById(1L)
                ).thenReturn(null);

       assertThrows(IllegalArgumentException.class, () -> {
            bookServiceToTest.deleteBookById(1L);
        });

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
        book.setId(1L);
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
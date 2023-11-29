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
    @Mock
    private CloudinaryConfig mockCloudinary;

    @Mock
    private ModelMapper mockModelMapper;

    @Mock
    private BookService bookServiceToTest;

    @BeforeEach
    void setUp() {
        bookServiceToTest = new BookServiceImpl(mockBookRepository, mockCategoryRepository, mockModelMapper, mockCloudinary);
    }

    @AfterEach
    void tearDown() {
        bookServiceToTest = null;
    }

    @Test
    void testSaveBookWithException() {
        //Arrange
        Book book = createBook();
        when(mockBookRepository.save(book))
                .thenReturn(null);
        //Act
        bookServiceToTest.saveBook(book);
        //Assert
      assertThrows(IllegalArgumentException.class, () -> bookServiceToTest.saveBook(null));
    }

    @Test
    void testSaveBook() {
        //Arrange
        Book book = createBook();
        when(mockBookRepository.save(book))
                .thenReturn(book);
        //Act
        Book resultBook = bookServiceToTest.saveBook(book);
        //Assert
        assertEquals(book, resultBook);
        verify(mockBookRepository, times(1)).save(book);
    }

    @Test
    void testFindBookById() {
        //Arrange
        Book book = createBook();
        when(mockBookRepository.findBookById(1L))
                .thenReturn(book);
        //Act
        Book resultBook = bookServiceToTest.findBookById(1L);
        //Assert
        assertEquals(book.getAuthor(), resultBook.getAuthor());
        assertEquals(book.getCategory().getCategoryName(), resultBook.getCategory().getCategoryName());
    }

    @Test
    void testDeleteBookById() {
        //Arrange
        when(mockBookRepository.findBookById(1L))
                .thenReturn(null);
        //Act
        Exception exception = new Exception();
        try {
            bookServiceToTest.deleteBookById(1L);
        } catch (Exception e) {
            exception = e;
        }
        //Assert
        assertEquals(exception.getMessage(), "Book with the given id was not found!");
    }

    @Test
    void testFindAll() throws IOException {
       //Arrange
        Book book = createBook();
        when(mockBookRepository.findAll())
                .thenReturn(List.of(book));
        //Act
        List<BookBindingModel> books = bookServiceToTest.findAll();
        //Assert
        assertEquals(1, books.size(), "Books count is not correct!");
        assertEquals("Author", books.get(0).getAuthor(), "Author is not correct!");
    }

    @Test
    void testMapBookToBookBindingModel() {
        //Arrange
        Book book = createBook();
        //Act
        BookBindingModel bookBindingModel = bookServiceToTest.mapBookToBookBindingModel(book);
        //Assert
        assertEquals(book.getAuthor(), bookBindingModel.getAuthor(), "Author is not correct!");
        assertEquals(book.getCategory().getCategoryName(), bookBindingModel.getCategory(), "Category is not correct!");
        assertEquals(book.getDescription(), bookBindingModel.getDescription(), "Description is not correct!");
        assertEquals(book.getTitle(), bookBindingModel.getTitle(), "Title is not correct!");
    }

    @Test
    void testBlurrySearch() {
        //Arrange
        Book book = createBook();
        when(mockBookRepository.findByTitleContaining("T"))
                .thenReturn(List.of(book));
        //Act
        List<Book> books = bookServiceToTest.blurrySearch("T");
        //Assert
        assertEquals(1, books.size(), "Books count is not correct!");
        assertEquals("Author", books.get(0).getAuthor(), "Author is not correct!");
            }

    @Test
    void testFindByCategory() {
        //Arrange
        Book book = createBook();
        when(mockBookRepository.findAllByCategoryCategoryName(CategoryName.ENGINEERING))
                .thenReturn(List.of(book));
        //Act
        List<Book> books = bookServiceToTest.findByCategory(CategoryName.ENGINEERING);
        //Assert
        assertEquals(1, books.size(), "Books count is not correct!");
        assertEquals("Author", books.get(0).getAuthor(), "Author is not correct!");
    }

    @Test
    void findAllBooks() {
        //Arrange
        Book book = createBook();
        when(mockBookRepository.findAll())
                .thenReturn(List.of(book));
        //Act
        List<Book> books = bookServiceToTest.findAllBooks();
        //Assert
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
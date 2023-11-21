package org.softuni.bookshopbg.service;


import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface BookService {
	
	Book saveWithDTO(BookBindingModel book) throws IOException, ParseException;

	List<BookBindingModel> findAll() throws IOException;


	List<Book> findAllBooks();

    Book deleteBookById(Long id);

	Page<BookBindingModel> getAllBooks(Pageable pageable);

	BookBindingModel mapBookToBookBindingModel(Book book);


	List<Book> blurrySearch(String keyword);

	List<Book> findByCategory(CategoryName category);

	Book saveBook(Book book);


	Book findBookById(Long id);

	String getCloudUrl(BookBindingModel bookBindingModel, MultipartFile imageToCloud) throws IOException;
}

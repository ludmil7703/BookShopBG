package org.softuni.bookshopbg.service;


import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.model.enums.CategoryName;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface BookService {
	
	Book save(BookBindingModel book) throws IOException, ParseException;

	List<Book> findAll() throws IOException;
	
	Book findById(Long id);


    void deleteBookById(Long id);

	BookBindingModel mapBookToBookBindingModel(Book book);


	List<Book> blurrySearch(String keyword);

	List<Book> findByCategory(CategoryName category);

	Book saveBook(Book book);

	List<BookBindingModel> getAllBooks() throws IOException;

	Optional<BookBindingModel> findBookById(Long id);
}

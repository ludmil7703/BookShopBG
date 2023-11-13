package org.softuni.bookshopbg.service;


import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface BookService {
	
	Book save(BookBindingModel book) throws IOException, ParseException;

	List<BookBindingModel> findAll() throws IOException;



    void deleteBookById(Long id);

	Page<BookBindingModel> getAllBooks(Pageable pageable);

	BookBindingModel mapBookToBookBindingModel(Book book);


	List<Book> blurrySearch(String keyword);

	List<Book> findByCategory(CategoryName category);

	void saveBook(Book book);


	Optional<BookBindingModel> findBookById(Long id);
}

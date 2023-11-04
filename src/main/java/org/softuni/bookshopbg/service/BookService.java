package org.softuni.bookshopbg.service;


import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Book;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface BookService {
	
	Book save(BookBindingModel book) throws IOException, ParseException;

	List<Book> findAll() throws IOException;
	
	Book findById(Long id);

	void deleteById(long l);

}

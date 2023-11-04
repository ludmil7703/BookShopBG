package org.softuni.bookshopbg.service.impl;

import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Author;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.model.entities.ImageData;
import org.softuni.bookshopbg.repositories.AuthorRepository;
import org.softuni.bookshopbg.repositories.CategoryRepository;
import org.softuni.bookshopbg.repositories.ImageDataRepository;
import org.softuni.bookshopbg.service.BookService;
import org.softuni.bookshopbg.utils.ImageUtil;
import org.springframework.stereotype.Service;

import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.repositories.BookRepository;


import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
	

	private BookRepository bookRepository;

	private AuthorRepository authorRepository;

	private CategoryRepository categoryRepository;

	private ImageDataRepository imageDataRepository;


	public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository, ImageDataRepository imageDataRepository) {
		this.authorRepository = authorRepository;
		this.bookRepository = bookRepository;
		this.categoryRepository = categoryRepository;
		this.imageDataRepository = imageDataRepository;
	}
	
	public Book save(BookBindingModel bookBindingModel) throws IOException, ParseException {
		Book book = new Book();
		Author author = new Author();
		String[] names = bookBindingModel.getAuthor().split("\\s+");
		author.setFirstName(names[0]);
		author.setLastName(names[1]);
		authorRepository.save(author);

		Category category = categoryRepository.findCategoryByCategoryName(bookBindingModel.getCategory());

		byte[] image = bookBindingModel.getBookImage().getBytes();
		ImageData imageData = new ImageData();
		imageData.setName(bookBindingModel.getBookImage().getOriginalFilename());
		imageData.setType(bookBindingModel.getBookImage().getContentType());
		imageData.setImageData(ImageUtil.compressImage(image));
		imageDataRepository.save(imageData);

		book.setTitle(bookBindingModel.getTitle());
		book.setAuthor(author);
		book.setDescription(bookBindingModel.getDescription());
		book.setListPrice(bookBindingModel.getListPrice());
		book.setOurPrice(bookBindingModel.getOurPrice());
		book.setInStockNumber(bookBindingModel.getInStockNumber());
		book.setActive(book.getInStockNumber());
		book.setCategory(category);
		book.setShippingWeight(bookBindingModel.getShippingWeight());
		book.setFormat(bookBindingModel.getFormat());
		book.setIsbn(bookBindingModel.getIsbn());
		book.setLanguage(bookBindingModel.getLanguage());
		book.setNumberOfPages(bookBindingModel.getNumberOfPages());
		book.setPublicationDate(new SimpleDateFormat("yyyy-MM-dd").parse((bookBindingModel.getPublicationDate().toString())));
		book.setPublisher(bookBindingModel.getPublisher());
		book.setBookImage(imageData);

		return bookRepository.save(book);
	}



	
	public List<Book> findAll() {
		return (List<Book>) bookRepository.findAll();
	}
	
	public Book findById(Long id) {
		return bookRepository.findById(id).orElse(null);
	}

	@Override
	public void deleteById(long l) {
		bookRepository.deleteById(l);
	}


}

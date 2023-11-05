package org.softuni.bookshopbg.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Author;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.repositories.AuthorRepository;
import org.softuni.bookshopbg.repositories.CategoryRepository;
import org.softuni.bookshopbg.service.BookService;
import org.springframework.stereotype.Service;

import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.repositories.BookRepository;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {
	

	private BookRepository bookRepository;

	private AuthorRepository authorRepository;

	private CategoryRepository categoryRepository;





	public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository) {
		this.authorRepository = authorRepository;
		this.bookRepository = bookRepository;
		this.categoryRepository = categoryRepository;

	}
	
	public Book save(BookBindingModel bookBindingModel) throws IOException, ParseException {
		Book book = new Book();
		Author author = new Author();
		String[] names = bookBindingModel.getAuthor().split("\\s+");
		author.setFirstName(names[0]);
		author.setLastName(names[1]);
		authorRepository.save(author);


		MultipartFile imageToCloud = bookBindingModel.getBookImage();



		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
				"cloud_name", "de2t3mhgr",
				"api_key", "319136679127269",
				"api_secret", "CyqB-VhB40myASzDFaN3aIp1JgQ",
				"secure", true));

			String  imageUrl = cloudinary.uploader().upload(imageToCloud.getBytes(),
			Map.of("public_id", "bookshopbg/" + bookBindingModel.getTitle()))
					.get("url")
					.toString();

			bookBindingModel.setImageUrl(imageUrl);


		Category category = categoryRepository.findCategoryByCategoryName(bookBindingModel.getCategory());

		ModelMapper modelMapper = new ModelMapper();

		book.setAuthor(author);
		book.setCategory(category);
		modelMapper.map(bookBindingModel, book);


		return bookRepository.save(book);
	}



	
	public List<BookBindingModel> findAll() {
		List<BookBindingModel> bookBindingModelList = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		for (Book book : (List<Book>) bookRepository.findAll()) {
			bookBindingModelList.add(modelMapper.map(book, BookBindingModel.class));
		}

		return bookBindingModelList;

	}
	
	public Book findById(Long id) {
		return bookRepository.findById(id).orElse(null);
	}

	@Override
	public void deleteBookById(Long id) {
		Optional<Book> byId = bookRepository.findById(id);
		if (byId.isPresent()){
			Book book = byId.get();
			book.getAuthor().getBooks().remove(book);
			book.getCategory().getBooks().remove(book);
		}

		bookRepository.deleteBookById(id);
	}



}

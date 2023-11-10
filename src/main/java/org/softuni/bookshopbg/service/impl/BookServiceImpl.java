package org.softuni.bookshopbg.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.repositories.CategoryRepository;
import org.softuni.bookshopbg.service.BookService;
import org.springframework.stereotype.Service;

import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.repositories.BookRepository;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {
	

	private BookRepository bookRepository;


	private CategoryRepository categoryRepository;


	private ModelMapper modelMapper;



	public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
		this.bookRepository = bookRepository;
		this.categoryRepository = categoryRepository;

		this.modelMapper = modelMapper;
	}
	
	public Book save(BookBindingModel bookBindingModel) throws IOException {
		Book book = new Book();


		if(!bookBindingModel.getBookImage().isEmpty()){
			MultipartFile imageToCloud = bookBindingModel.getBookImage();

			Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
					"cloud_name", "de2t3mhgr",
					"api_key", "319136679127269",
					"api_secret", "CyqB-VhB40myASzDFaN3aIp1JgQ",
					"secure", true));

			String imageUrl = cloudinary.uploader().upload(imageToCloud.getBytes(),
							Map.of("public_id", "bookshopbg/" + bookBindingModel.getTitle()))
					.get("url")
					.toString();

			bookBindingModel.setImageUrl(imageUrl);
		} else {
			bookRepository.findById(bookBindingModel.getId()).ifPresent(book1 -> bookBindingModel.setImageUrl(book1.getImageUrl()));
		}


		Category category = categoryRepository.findCategoryByCategoryName(bookBindingModel.getCategory());

		book.setCategory(category);
		modelMapper.map(bookBindingModel, book);


		return bookRepository.save(book);
	}

	public Book saveBook(Book book) {
		return bookRepository.save(book);
	}



	
	public List<Book> findAll() {
		return bookRepository.findAll();

	}
	
	public Book findById(Long id) {
		return bookRepository.findById(id).orElse(null);
	}

	@Override
	public void deleteBookById(Long id) {

		bookRepository.deleteBookById(id);
	}

	public BookBindingModel mapBookToBookBindingModel(Book book) {
		BookBindingModel bookBindingModel = new BookBindingModel();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.map(book, bookBindingModel);
		return bookBindingModel;
	}


	public List<Book> blurrySearch(String title) {
		List<Book> bookList = bookRepository.findByTitleContaining(title);
		List<Book> activeBookList = new ArrayList<>();

		for (Book book: bookList) {
			if(book.isActive()) {
				activeBookList.add(book);
			}
		}

		return activeBookList;
	}

	@Override
	public List<Book> findByCategory(CategoryName category) {

		return bookRepository.findAllByCategoryCategoryName(category);
	}


}

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
import org.softuni.bookshopbg.service.aop.WarnIfExecutionExceeds;
import org.softuni.bookshopbg.utils.CloudinaryConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.repositories.BookRepository;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BookServiceImpl implements BookService {
	

	private BookRepository bookRepository;


	private CategoryRepository categoryRepository;


	private ModelMapper modelMapper;

	private CloudinaryConfig cloudinary;



	public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository, ModelMapper modelMapper, CloudinaryConfig cloudinary) {
		this.bookRepository = bookRepository;
		this.categoryRepository = categoryRepository;
		this.modelMapper = modelMapper;
		this.cloudinary = cloudinary;
	}

	@Override
	public Book saveWithDTO(BookBindingModel bookBindingModel) throws IOException {
		Book book = new Book();

		MultipartFile imageToCloud = bookBindingModel.getBookImage();


		if(imageToCloud != null && imageToCloud.getSize() > 0) {
			imageToCloud = bookBindingModel.getBookImage();


			String imageUrl = getCloudUrl(bookBindingModel, imageToCloud);


			bookBindingModel.setImageUrl(imageUrl);
		}


		Category category = categoryRepository.findCategoryByCategoryName(bookBindingModel.getCategory());

		book.setCategory(category);
		modelMapper.map(bookBindingModel, book);


		return bookRepository.save(book);
	}


	@Override
	public Book saveBook(Book book) {
		if (book == null) {
			throw new IllegalArgumentException("Book cannot be null!");
		}
		return bookRepository.save(book);
	}



	@Override
	public List<BookBindingModel> findAll() {
		return bookRepository.findAll().stream().map(this::mapBookToBookBindingModel).toList();

	}

	public List<Book> findAllBooks() {
		return bookRepository.findAll();
	}


	@Override
	public Book findBookById(Long id) {
		Book book = bookRepository.findBookById(id);
		return book;
	}

	@Override
	public Book deleteBookById(Long id) {
		Book book = bookRepository.findBookById(id);
		if (book == null) {
			throw new IllegalArgumentException("Book with the given id was not found!");
		}
		bookRepository.delete(book);
		return book;
	}
	@Override
	public Page<BookBindingModel> getAllBooks(Pageable pageable) {
		return bookRepository
				.findAll(pageable)
				.map(this::mapBookToBookBindingModel);
	}



	public BookBindingModel mapBookToBookBindingModel(Book book) {
		BookBindingModel bookBindingModel = new BookBindingModel();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.map(book, bookBindingModel);
		return bookBindingModel;
	}

	@WarnIfExecutionExceeds(
			timeInMillis = 1000L
	)
	@Override
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

	@WarnIfExecutionExceeds(
			timeInMillis = 1000L
	)
	@Override
	public List<Book> findByCategory(CategoryName category) {

		return bookRepository.findAllByCategoryCategoryName(category);
	}

	public String getCloudUrl(BookBindingModel bookBindingModel, MultipartFile imageToCloud) throws IOException {

		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
				"cloud_name", "de2t3mhgr",
				"api_key", "319136679127269",
				"api_secret", "CyqB-VhB40myASzDFaN3aIp1JgQ"));


		String imageUrl = cloudinary.uploader().upload(imageToCloud.getBytes(),
						Map.of("public_id", "bookshopbg/" + bookBindingModel.getTitle()))
				.get("url")
				.toString();


		return imageUrl;
	}



}

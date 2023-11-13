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

	@Override
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

	@Override
	public void saveBook(Book book) {
		bookRepository.save(book);
	}



	@Override
	public List<BookBindingModel> findAll() {
		return bookRepository.findAll().stream().map(this::mapBookToBookBindingModel).toList();

	}


	@Override
	public Optional<BookBindingModel> findBookById(Long id) {
		return bookRepository
				.findById(id)
				.map(this::mapBookToBookBindingModel);
	}

	@Override
	public void deleteBookById(Long id) {

		bookRepository.deleteBookById(id);
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

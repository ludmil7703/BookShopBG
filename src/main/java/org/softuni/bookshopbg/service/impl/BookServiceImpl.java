package org.softuni.bookshopbg.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service
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


			book.setImageUrl(imageUrl);


		Category category = categoryRepository.findCategoryByCategoryName(bookBindingModel.getCategory());


		book.setTitle(bookBindingModel.getTitle());
		book.setAuthor(author);
		book.setDescription(bookBindingModel.getDescription());
		book.setListPrice(bookBindingModel.getListPrice());
		book.setOurPrice(bookBindingModel.getOurPrice());
		book.setInStockNumber(bookBindingModel.getInStockNumber());
		book.setActive(bookBindingModel.isActive());
		book.setCategory(category);
		book.setShippingWeight(bookBindingModel.getShippingWeight());
		book.setFormat(bookBindingModel.getFormat());
		book.setIsbn(bookBindingModel.getIsbn());
		book.setLanguage(bookBindingModel.getLanguage());
		book.setNumberOfPages(bookBindingModel.getNumberOfPages());
		book.setReleaseDate(new SimpleDateFormat("yyyy-MM-dd").parse((bookBindingModel.getPublicationDate().toString())));
		book.setPublisher(bookBindingModel.getPublisher());

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

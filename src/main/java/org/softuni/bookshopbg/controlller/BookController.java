package org.softuni.bookshopbg.controlller;


import jakarta.servlet.http.HttpServletRequest;

import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.repositories.CategoryRepository;
import org.softuni.bookshopbg.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookService bookService;

	@Autowired
	private final CategoryRepository categoryRepository;

	public BookController(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addBook(Model model) {
		BookBindingModel book = new BookBindingModel();
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("book", book);
		model.addAttribute("categoryList", categoryList);
		return "addBook";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addBookPost(@ModelAttribute("book") BookBindingModel book, HttpServletRequest request) throws IOException, ParseException {
		bookService.save(book);

		return "redirect:/books/bookList";
	}
	
	@RequestMapping("/bookInfo")
	public String bookInfo(@RequestParam("id") Long id, Model model) {
		Book book = bookService.findById(id);
		model.addAttribute("book", book);
		
		return "bookInfo";
	}
	
	
	@RequestMapping("/updateBook")
	public String updateBook(@RequestParam("id") Long id, Model model) {
		Book book = bookService.findById(id);
		model.addAttribute("book", book);
		
		return "updateBook";
	}
	
	
	@RequestMapping(value="/updateBook", method=RequestMethod.POST)
	public String updateBookPost(@ModelAttribute("book") BookBindingModel book, HttpServletRequest request) throws IOException, ParseException {
		bookService.save(book);


		
		return "redirect:/book/bookInfo?id="+book.getId();
	}
	
	@RequestMapping("/bookList")
	public String bookList(Model model) throws IOException {
		List<Book> bookList = bookService.findAll();
		model.addAttribute("bookList", bookList);
		return "bookList";
		
	}
	
	@RequestMapping(value="/remove", method=RequestMethod.POST)
	public String remove(
			@ModelAttribute("id") String id, Model model
			) throws IOException {
		bookService.deleteById(Long.parseLong(id.substring(8)));
		List<Book> bookList = bookService.findAll();
		model.addAttribute("bookList", bookList);
		
		return "redirect:/books/bookList";
	}



}

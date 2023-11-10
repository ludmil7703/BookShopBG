package org.softuni.bookshopbg.controlller;


import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.Valid;
import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.dto.UserLoginBindingModel;
import org.softuni.bookshopbg.model.dto.UserRegisterBindingModel;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.repositories.CategoryRepository;
import org.softuni.bookshopbg.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
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

	@GetMapping(value = "/add")
	public String addBook(Model model) {
		List<Category> categoryList = categoryRepository.findAll();
		if (!model.containsAttribute("book")){
			model.addAttribute("book", new BookBindingModel());
		}
		model.addAttribute("categoryList", categoryList);
		return "addBook";
	}

	@PostMapping(value = "/add")
	public String addBookPost(@Valid @ModelAttribute("book") BookBindingModel book, BindingResult bindingResult,
							  RedirectAttributes rAtt) throws IOException, ParseException {
		if (bindingResult.hasErrors()){
			rAtt.addFlashAttribute("book", book);
			rAtt.addFlashAttribute("org.springframework.validation.BindingResult.book", bindingResult);
			return "redirect:/books/add";
		}
		bookService.save(book);

		return "redirect:/books/bookList";
	}
	
	@GetMapping("/bookInfo/{id}")
	public String bookInfo(@PathVariable Long id, Model model) {
		Book book = bookService.findById(id);
		model.addAttribute("book", book);
		
		return "bookInfo";
	}
	
	
	@RequestMapping("/updateBook/{id}")
	public String updateBook(@PathVariable Long id, Model model) {
		Book book = bookService.findById(id);
		BookBindingModel bookBindingModel = bookService.mapBookToBookBindingModel(book);
		model.addAttribute("book", bookBindingModel);
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		
		return "updateBook";
	}
	
	
	@RequestMapping(value="/updateBook", method=RequestMethod.POST)
	public String updateBook(@Valid @ModelAttribute("book") BookBindingModel book, BindingResult bindingResult,
							  RedirectAttributes rAtt) throws IOException, ParseException {
		if (bindingResult.hasErrors()){
			rAtt.addFlashAttribute("book", book);
			rAtt.addFlashAttribute("org.springframework.validation.BindingResult.book", bindingResult);
			return "redirect:/books/add";
		}
		bookService.save(book);


		
		return "redirect:/books/bookInfo/"+book.getId();
	}
	
	@RequestMapping("/bookList")
	public String bookList(Model model) throws IOException {
		List<Book> list = bookService.findAll();
		List<BookBindingModel> bookList = new ArrayList<>();
		for (Book book : list) {
			bookList.add(bookService.mapBookToBookBindingModel(book));
		}
		model.addAttribute("bookList", bookList);
		return "bookList";
		
	}

@RequestMapping(value="/remove", method=RequestMethod.POST)
public String remove(
		@ModelAttribute("id") String id, Model model
) throws IOException {
	bookService.deleteBookById(Long.parseLong(id.substring(8)));
	List<Book> bookList = bookService.findAll();
	model.addAttribute("bookList", bookList);

	return "redirect:/book/bookList";
}


}

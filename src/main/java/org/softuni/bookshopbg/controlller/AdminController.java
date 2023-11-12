package org.softuni.bookshopbg.controlller;


import jakarta.validation.Valid;
import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.service.BookService;
import org.softuni.bookshopbg.service.CategoryService;
import org.softuni.bookshopbg.exception.ObjectNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/books")
public class AdminController {


	private final BookService bookService;


	private final CategoryService categoryService;


	public AdminController(BookService bookService, CategoryService categoryService) {
		this.bookService = bookService;
		this.categoryService = categoryService;
	}

	@GetMapping(value = "/add")
	public String addBook(Model model) {
		List<Category> categoryList = categoryService.getAllCategories();
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
		BookBindingModel book = bookService.findBookById(id)
				.orElseThrow(() -> new ObjectNotFoundException("Invalid book Id:" + id));
		model.addAttribute("book", book);
		
		return "bookInfo";
	}
	
	
	@RequestMapping("/updateBook/{id}")
	public String updateBook(@PathVariable Long id, Model model) {
		BookBindingModel book = bookService.findBookById(id)
				.orElseThrow(() -> new ObjectNotFoundException("Invalid book Id:" + id));
		model.addAttribute("book", book);
		List<Category> categoryList = categoryService.getAllCategories();
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
		List<BookBindingModel> bookList = bookService.findAll();
		model.addAttribute("bookList", bookList);
		return "bookList";
		
	}

@DeleteMapping(value="/remove/{id}")
public String remove(@PathVariable Long id, Model model) throws IOException {
	bookService.deleteBookById(id);
	List<BookBindingModel> bookList = bookService.findAll();
	model.addAttribute("bookList", bookList);

	return "redirect:/book/bookList";
}


}

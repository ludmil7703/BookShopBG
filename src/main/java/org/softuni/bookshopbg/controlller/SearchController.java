package org.softuni.bookshopbg.controlller;

import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.repositories.CategoryRepository;
import org.softuni.bookshopbg.service.BookService;
import org.softuni.bookshopbg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Controller
public class SearchController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private BookService bookService;

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping("/search")
	public String search(Model model){
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		List<Book> bookShelf = new ArrayList<>();
		model.addAttribute("bookShelf", bookShelf);
		return "bookshelf";
	}
	@RequestMapping("/searchByCategory")
	public String searchByCategory(
			@RequestParam("category") CategoryName category,
			Model model, Principal principal
			){
		if(principal!=null) {
			String username = principal.getName();
			UserEntity user = userService.findUserByUsername(username).orElse(null);
			model.addAttribute("user", user);
		}
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);

		
		List<Book> bookShelf = bookService.findByCategory(category);
		
		if (bookShelf.isEmpty()) {
			model.addAttribute("emptyList", true);
			model.addAttribute("bookShelf", bookShelf);
			return "bookshelf";
		}
		
		model.addAttribute("bookShelf", bookShelf);
		
		return "bookshelf";
	}
	
	@RequestMapping("/searchBook")
	public String searchBook(
			@ModelAttribute("keyword") String keyword,
			Principal principal, Model model
			) {
		if(principal!=null) {
			String username = principal.getName();
			UserEntity user = userService.findUserByUsername(username).orElse(null);
			model.addAttribute("user", user);
		}
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		List<Book> bookShelf = bookService.blurrySearch(keyword);
		
		if (bookShelf.isEmpty()) {
			model.addAttribute("emptyList", true);
			model.addAttribute("bookShelf", bookShelf);
			return "bookshelf";
		}
		
		model.addAttribute("bookShelf", bookShelf);
		
		return "bookshelf";
	}
}

package org.softuni.bookshopbg.controlller;

import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.service.BookService;
import org.softuni.bookshopbg.service.CategoryService;
import org.softuni.bookshopbg.service.UserService;
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
	private final UserService userService;

	private final BookService bookService;

	private final CategoryService categoryService;

	public SearchController(UserService userService,
							BookService bookService,
							CategoryService categoryService) {
		this.userService = userService;
		this.bookService = bookService;
		this.categoryService = categoryService;
	}

	@GetMapping("/search")
	public String search(Model model){
		List<Category> categoryList = categoryService.getAllCategories();
		model.addAttribute("categoryList", categoryList);
		List<Book> bookList = new ArrayList<>();
		model.addAttribute("bookList", bookList);
		return "bookSearch";
	}
	@RequestMapping("/searchByCategory")
	public String searchByCategory(
			@RequestParam("category") CategoryName category,
			Model model, Principal principal
	){
		searchBook(model, principal);


		List<Book> bookList = bookService.findByCategory(category);

		if (bookList.isEmpty()) {
			model.addAttribute("emptyList", true);
			model.addAttribute("bookList", bookList);
			return "bookList";
		}

		model.addAttribute("bookList", bookList);

		return "bookList";
	}



	@RequestMapping("/searchBook")
	public String searchBook(
			@ModelAttribute("keyword") String keyword,
			Principal principal, Model model
	) {
		searchBook(model, principal);
		List<Book> bookList = bookService.blurrySearch(keyword);

		if (bookList.isEmpty()) {
			model.addAttribute("emptyList", true);
			model.addAttribute("bookList", bookList);
			return "bookSearch";
		}

		model.addAttribute("bookList", bookList);

		return "bookSearch";
	}

	private void searchBook(Model model, Principal principal) {
		if(principal!=null) {
			String username = principal.getName();
			UserEntity user = userService.findUserByUsername(username);
			model.addAttribute("user", user);
		}
		List<Category> categoryList = categoryService.getAllCategories();
		model.addAttribute("categoryList", categoryList);
	}
}
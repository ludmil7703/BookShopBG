package org.softuni.bookshopbg.controlller;

import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.repositories.BookRepository;
import org.softuni.bookshopbg.repositories.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@CrossOrigin(origins = "*")
@Controller
public class HomeController {
    private final CategoryRepository categoryRepository;

    private final BookRepository bookRepository;

    public HomeController(CategoryRepository categoryRepository, BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Category> categoryList = categoryRepository.findAll();
        List<Book> bookShelf = bookRepository.findAll();
        model.addAttribute("bookShelf", bookShelf);
        model.addAttribute("categoryList", categoryList);
        return "/index";
    }

    @GetMapping("/admin")
    public String home() {
        return "bookList";
    }
}

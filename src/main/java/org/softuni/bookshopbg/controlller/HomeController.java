package org.softuni.bookshopbg.controlller;

import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.repositories.BookRepository;
import org.softuni.bookshopbg.repositories.CategoryRepository;
import org.softuni.bookshopbg.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
    private final CategoryRepository categoryRepository;

    private final BookRepository bookRepository;

    private final UserService userService;

    public HomeController(CategoryRepository categoryRepository, BookRepository bookRepository, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Category> categoryList = categoryRepository.findAll();
        List<Book> bookShelf = bookRepository.findAll();
        model.addAttribute("bookShelf", bookShelf);
        model.addAttribute("categoryList", categoryList);
        return "/index";
    }

    @GetMapping("/subpage")
    public String subpage(Model model) {
        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("categoryList", categoryList);
        return "/subpage";
    }
    @GetMapping("/admin")
    public String home() {
        return "admin";
    }

    @RequestMapping("/bookDetail/{id}")
    public String bookDetails(@PathVariable Long id, Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Optional<UserEntity> user = userService.findUserByUsername(username);
            model.addAttribute("user", user);
        }
        Book book = bookRepository.findById(id).orElse(null);
        model.addAttribute("book", book);
        List<Integer> qtyList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);

        model.addAttribute("qtyList", qtyList);
        model.addAttribute("qty", 1);
        return "bookDetail";
    }

    @GetMapping("/faq")
    public String faq(){
        return "faq";
    }
}

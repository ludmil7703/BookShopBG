package org.softuni.bookshopbg.controlller;

import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.service.BookService;
import org.softuni.bookshopbg.service.CategoryService;
import org.softuni.bookshopbg.exception.ObjectNotFoundException;
import org.softuni.bookshopbg.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
    private final CategoryService categoryService;

    private final BookService bookService;

    private final UserService userService;



    public HomeController(CategoryService categoryService,
                          BookService bookService,
                          UserService userService) {
        this.categoryService = categoryService;
        this.bookService = bookService;
        this.userService = userService;

    }



    @GetMapping("/")
    public String index(Model model) throws IOException {
        List<Category> categoryList = categoryService.getAllCategories();
        List<BookBindingModel> bookShelf = bookService.findAll();
        model.addAttribute("bookShelf", bookShelf);
        model.addAttribute("categoryList", categoryList);
        return "/index";
    }

//    @GetMapping("/allBooks")
//    public String allBooks(Model model, @PageableDefault(size = 2, sort = "id") Pageable pageable) {
//        List<Category> categoryList = categoryService.getAllCategories();
//
//        Page<BookBindingModel> bookShelf = bookService.getAllBooks(pageable);
//
//        model.addAttribute("bookShelf", bookShelf);
//        model.addAttribute("categoryList", categoryList);
//        return "index";
//    }

    @GetMapping("/contact")
    public String contact(Model model){
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        return "contact";
    }

    @GetMapping("/subpage")
    public String subpage(Model model) {
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        return "subpage";
    }


    @RequestMapping("/bookDetail/{id}")
    public String bookDetails(@PathVariable Long id, Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Optional<UserEntity> user = userService.findUserByUsername(username);
            model.addAttribute("user", user);
        }
        Book book = bookService.findBookById(id);
        model.addAttribute("book", book);
        List<Integer> qtyList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);

        model.addAttribute("qtyList", qtyList);
        model.addAttribute("qty", 1);
        return "bookDetail";
    }
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/faq")
    public String faq(){
        return "faq";
    }
}

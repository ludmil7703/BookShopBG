package org.softuni.bookshopbg.controlller;

import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/books")
public class BookRestController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookBindingModel>> getAllBooks() throws IOException {
        return ResponseEntity.ok(this.bookService.getAllBooks());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookBindingModel> deleteBookById(@PathVariable Long id) {
        this.bookService.deleteBookById(id);
        return ResponseEntity.ok().build();
    }
}
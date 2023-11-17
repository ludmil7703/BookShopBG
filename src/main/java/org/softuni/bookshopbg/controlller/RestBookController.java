package org.softuni.bookshopbg.controlller;

import org.softuni.bookshopbg.exception.ObjectNotFoundRestException;
import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/books")
public class RestBookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookBindingModel>> getAllBooks() {

        System.out.println("Inside Controller");
        try {
            List<BookBindingModel> books = this.bookService.findAll();
            return ResponseEntity.ok().body(books);
        } catch (ObjectNotFoundRestException | IOException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<BookBindingModel> deleteBookById(@PathVariable Long id, Model model) {
        try {
            this.bookService.deleteBookById(id);
        } catch (ObjectNotFoundRestException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
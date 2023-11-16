package org.softuni.bookshopbg.repositories;

import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {



    Book deleteBookById(Long id);

    List<Book> findByTitleContaining(String title);

    List<Book> findAllByCategoryCategoryName(CategoryName categoryName);

    Book findBookById(Long id);

    Book saveBook(Book book);
}

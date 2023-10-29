package org.softuni.bookshopbg.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "authors")
public class Author extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @OneToMany
    @JoinTable(name = "authors_books",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    public List<Book> books;

    public String getAuthorFullName() {
        return this.firstName + " " + this.lastName;
    }

    public String getAuthorFullNameAndCountOfBooks() {
        return this.firstName + " " + this.lastName + " " + this.books.size();
    }

}
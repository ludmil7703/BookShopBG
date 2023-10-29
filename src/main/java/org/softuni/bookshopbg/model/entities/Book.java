package org.softuni.bookshopbg.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.softuni.bookshopbg.model.enums.AgeRestriction;
import org.softuni.bookshopbg.model.enums.EditionType;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "books")
public class Book extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 100)
    private String description;

    @Enumerated(EnumType.STRING)
    private EditionType editionType;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer copies;

    @Column(name = "release_date")
    @DateTimeFormat(pattern = "dd-MM-yyyy' 'HH:mm")
    private Date releaseDate;

    @Enumerated(EnumType.STRING)
    private AgeRestriction ageRestriction;

    @ManyToOne
    private Author author;

    @ManyToMany(mappedBy = "books", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Category> categories;

    @ManyToMany(mappedBy = "books", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Order> orders;


}
package org.softuni.bookshopbg.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity{

    @ManyToOne
    private User user;

    @ManyToMany
    private List<Book> books;

    @DateTimeFormat(pattern = "dd-MM-yyyy' 'HH:mm")
    private Date orderDate;

    @Column(name = "order_status",nullable = false)
    private String orderStatus;

}

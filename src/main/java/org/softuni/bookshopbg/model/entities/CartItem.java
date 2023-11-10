package org.softuni.bookshopbg.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class CartItem extends BaseEntity{


    private int qty;
    private BigDecimal subtotal;

    @ManyToOne
    @JoinColumn(name="book_id", referencedColumnName = "id")
    private Book book;


    @ManyToOne
    @JoinColumn(name="shopping_cart_id")
    private ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;



}
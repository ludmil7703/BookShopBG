package org.softuni.bookshopbg.model.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;



@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookToCartItem extends BaseEntity {


    @ManyToOne
    private Book book;

    @ManyToOne
    private CartItem cartItem;



}
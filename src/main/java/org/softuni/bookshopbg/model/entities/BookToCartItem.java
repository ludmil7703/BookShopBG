package org.softuni.bookshopbg.model.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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


    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }


}
package org.softuni.bookshopbg.repositories;


import org.softuni.bookshopbg.model.entities.BookToCartItem;
import org.softuni.bookshopbg.model.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface BookToCartItemRepository extends JpaRepository<BookToCartItem, Long> {

    void deleteByCartItem(CartItem cartItem);

}
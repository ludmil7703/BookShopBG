package org.softuni.bookshopbg.model.entities;

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
public class ShoppingCart extends BaseEntity{

    private BigDecimal GrandTotal;

    @OneToMany(mappedBy="shoppingCart", cascade= CascadeType.ALL, fetch= FetchType.EAGER)
    private List<CartItem> cartItemList;

    @OneToOne(fetch = FetchType.EAGER)
    private UserEntity user;


    public void setGrandTotal(BigDecimal grandTotal) {
        GrandTotal = grandTotal;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }


}
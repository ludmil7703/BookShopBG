package org.softuni.bookshopbg.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.softuni.bookshopbg.model.security.UserRoleEntity;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity{
    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false, updatable = false)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<UserRoleEntity> roles = new ArrayList<>();

    private String password;

    private String firstName;

    private String lastName;

    private boolean active;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private ShoppingCart shoppingCart;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UserShipping> userShippingList;

    @OneToMany( mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UserPayment> userPaymentList;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Order> orderList;


public UserEntity(){
    this.shoppingCart = new ShoppingCart();
    this.orderList = new ArrayList<>();
    this.userShippingList = new ArrayList<>();
    this.userPaymentList = new ArrayList<>();
}


}
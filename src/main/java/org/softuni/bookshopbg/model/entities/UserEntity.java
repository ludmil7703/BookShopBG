package org.softuni.bookshopbg.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity{

    private String username;

    @Column(unique = true)
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserShipping> userShippingList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserPayment> userPaymentList;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Order> orderList;


public UserEntity(){
    this.shoppingCart = new ShoppingCart();
    this.orderList = new ArrayList<>();
    this.userShippingList = new ArrayList<>();
    this.userPaymentList = new ArrayList<>();
}

}
package org.softuni.bookshopbg.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity{
    @Column(name = "username", nullable = false, unique = true)
    @Length(min = 3, max = 20)
    private String username;

    @Column(name = "password", nullable = false)
//    @Length(min = 3, max = 20)
    private String password;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Order> orders;
}

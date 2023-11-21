package org.softuni.bookshopbg.model.security;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.softuni.bookshopbg.model.enums.UserRoleEnum;


@Getter
@Setter
@Table(name = "roles")
@Entity
public class UserRoleEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;


    public UserRoleEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public UserRoleEntity setRole(UserRoleEnum role) {
        this.role = role;
        return this;
    }
}
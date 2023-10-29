package org.softuni.bookshopbg.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class Address extends BaseEntity{

    private String country;
    private String city;
    
    private String street;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne
    private User user;

}

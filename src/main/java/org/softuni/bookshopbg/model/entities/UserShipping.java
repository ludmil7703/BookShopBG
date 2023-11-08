package org.softuni.bookshopbg.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.security.core.userdetails.User;


@Entity
@Table(name = "user_shipping")
public class UserShipping extends BaseEntity{
    private String userShippingName;
    private String userShippingStreet1;
    private String userShippingStreet2;
    private String userShippingCity;
    private String userShippingState;
    private String userShippingCountry;
    private String userShippingZipcode;
    private boolean userShippingDefault;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


    public String getUserShippingName() {
        return userShippingName;
    }


    public void setUserShippingName(String userShippingName) {
        this.userShippingName = userShippingName;
    }


    public String getUserShippingStreet1() {
        return userShippingStreet1;
    }


    public void setUserShippingStreet1(String userShippingStreet1) {
        this.userShippingStreet1 = userShippingStreet1;
    }


    public String getUserShippingStreet2() {
        return userShippingStreet2;
    }


    public void setUserShippingStreet2(String userShippingStreet2) {
        this.userShippingStreet2 = userShippingStreet2;
    }


    public String getUserShippingCity() {
        return userShippingCity;
    }


    public void setUserShippingCity(String userShippingCity) {
        this.userShippingCity = userShippingCity;
    }


    public String getUserShippingState() {
        return userShippingState;
    }


    public void setUserShippingState(String userShippingState) {
        this.userShippingState = userShippingState;
    }


    public String getUserShippingCountry() {
        return userShippingCountry;
    }


    public void setUserShippingCountry(String userShippingCountry) {
        this.userShippingCountry = userShippingCountry;
    }


    public String getUserShippingZipcode() {
        return userShippingZipcode;
    }


    public void setUserShippingZipcode(String userShippingZipcode) {
        this.userShippingZipcode = userShippingZipcode;
    }


    public UserEntity getUser() {
        return user;
    }


    public void setUser(UserEntity user) {
        this.user = user;
    }


    public boolean isUserShippingDefault() {
        return userShippingDefault;
    }


    public void setUserShippingDefault(boolean userShippingDefault) {
        this.userShippingDefault = userShippingDefault;
    }




}
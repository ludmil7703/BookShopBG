package org.softuni.bookshopbg.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;



public class UserRegisterBindingModel {

//    @UniqueUsername(message = "Username already exists!")
    @Length(min = 3, max = 20, message = "Username length must be between 3 and 20 characters!")
    private String username;

    private String firstName;

    private String lastName;
    @Email(message = "Email must be valid!")
    @NotBlank(message = "Email cannot be null!")
//    @UniqueEmail(message = "Email already exists!")
    private String email;
    @Length(min = 3, max = 20, message = "Password length must be between 3 and 20 characters!")
    private static String password;
    @Length(min = 3, max = 20, message = "Password length must be between 3 and 20 characters!")

    private String confirmPassword;
    private boolean active;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }


    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

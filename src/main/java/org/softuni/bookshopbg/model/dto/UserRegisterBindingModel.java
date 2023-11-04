package org.softuni.bookshopbg.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;


public class UserRegisterBindingModel {

//    @UniqueUsername(message = "Username already exists!")
    @Length(min = 3, max = 20, message = "Username length must be between 3 and 20 characters!")
    private String username;

    @Length(min = 3, max = 20, message = "First name length must be between 3 and 20 characters!")
    private String firstName;

    @Length(min = 3, max = 20, message = "Last name length must be between 3 and 20 characters!")
    private String lastName;

    @Email(message = "Email must be valid!")
    @NotBlank(message = "Email cannot be null!")
//    @UniqueEmail(message = "Email already exists!")
    private String email;

    @Length(min = 3, max = 20, message = "Password length must be between 3 and 20 characters!")
    private static String password;

    @Length(min = 3, max = 20, message = "Confirm password length must be between 3 and 20 characters!")
    private String confirmPassword;

    public UserRegisterBindingModel() {
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        UserRegisterBindingModel.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

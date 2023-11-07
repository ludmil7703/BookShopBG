package org.softuni.bookshopbg.service;


import org.softuni.bookshopbg.model.dto.UserLoginBindingModel;
import org.softuni.bookshopbg.model.dto.UserRegisterBindingModel;
import org.softuni.bookshopbg.model.entities.UserEntity;

import java.util.Optional;

public interface UserService {
    boolean register(UserRegisterBindingModel userRegisterBindingModel);


    void login(UserLoginBindingModel userLoginBindingModel);


    void logout();

    boolean isLogged();

    UserEntity findUserByEmail(String username);

    Optional<UserEntity> findUserByUsername(String username);

    boolean checkCredentials(String username, String password);

}

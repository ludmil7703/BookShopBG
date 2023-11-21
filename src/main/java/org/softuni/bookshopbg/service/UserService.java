package org.softuni.bookshopbg.service;


import org.softuni.bookshopbg.model.dto.UserLoginBindingModel;
import org.softuni.bookshopbg.model.dto.UserRegisterBindingModel;
import org.softuni.bookshopbg.model.entities.UserBilling;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.model.entities.UserPayment;
import org.softuni.bookshopbg.model.entities.UserShipping;
import org.softuni.bookshopbg.model.security.PasswordResetToken;

import java.util.Optional;
import java.util.Set;

public interface UserService {
//    boolean register(UserRegisterBindingModel userRegisterBindingModel);


    UserEntity findUserByEmail(String username);

    Optional<UserEntity> findUserByUsername(String username);

//    boolean checkCredentials(String username, String password);


    void createPasswordResetTokenForUser(UserEntity user, String token);

    UserEntity createUser(UserEntity user);

    PasswordResetToken getPasswordResetToken(final String token);


    UserEntity findById(Long id);


    UserEntity save(UserEntity user);

    void updateUserBilling(UserBilling userBilling, UserPayment userPayment, UserEntity user);

    void updateUserShipping(UserShipping userShipping, UserEntity user);

    void setUserDefaultPayment(Long userPaymentId, UserEntity user);

    void setUserDefaultShipping(Long userShippingId, UserEntity user);
}

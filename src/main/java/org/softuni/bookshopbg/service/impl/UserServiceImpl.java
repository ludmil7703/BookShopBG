package org.softuni.bookshopbg.service.impl;


import org.softuni.bookshopbg.model.dto.UserLoginBindingModel;
import org.softuni.bookshopbg.model.dto.UserRegisterBindingModel;
import org.softuni.bookshopbg.model.entities.ShoppingCart;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.model.enums.UserRoleEnum;
import org.softuni.bookshopbg.repositories.RoleRepository;
import org.softuni.bookshopbg.repositories.ShoppingCartRepository;
import org.softuni.bookshopbg.repositories.UserRepository;
import org.softuni.bookshopbg.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean register(
            UserRegisterBindingModel userRegisterBindingModel) {

        userRepository.save(map(userRegisterBindingModel));
        return true;
    }

    @Override
    public void login(UserLoginBindingModel userLoginBindingModel) {

    }

    private UserEntity map(UserRegisterBindingModel userRegisterBindingModel) {
        UserEntity user = new UserEntity();


        if (userRepository.count() == 0) {
            user.getRoles().add(roleRepository.findByRole(UserRoleEnum.ADMIN));
        } else {
            user.getRoles().add(roleRepository.findByRole(UserRoleEnum.USER));
        }
        user.setActive(true);
        user.setFirstName(userRegisterBindingModel.getFirstName());
        user.setLastName(userRegisterBindingModel.getLastName());
        user.setEmail(userRegisterBindingModel.getEmail());
        user.setUsername(userRegisterBindingModel.getUsername());
        user.setShoppingCart(new ShoppingCart());
        user.setUserShippingList(new ArrayList<>());
        user.setPassword(passwordEncoder.encode(UserRegisterBindingModel.getPassword()));
        return user;
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        Optional<UserEntity> user = this.userRepository.findByEmail(email);

        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    @Override
    public Optional<UserEntity> findUserByUsername(String username) {
        return this.userRepository.findByUsername(username);


    }

    @Override
    public boolean checkCredentials(String username, String password) {
        Optional<UserEntity> user = this.userRepository.findByUsername(username);

        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }

    @Override
    public void save(UserEntity userEntity) {
        this.userRepository.save(userEntity);
    }
}

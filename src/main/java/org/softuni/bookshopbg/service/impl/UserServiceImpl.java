package org.softuni.bookshopbg.service.impl;


import org.softuni.bookshopbg.model.dto.UserLoginBindingModel;
import org.softuni.bookshopbg.model.dto.UserRegisterBindingModel;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.model.enums.UserRoleEnum;
import org.softuni.bookshopbg.repositories.RoleRepository;
import org.softuni.bookshopbg.repositories.UserRepository;
import org.softuni.bookshopbg.service.UserService;
import org.softuni.bookshopbg.utils.LoggedUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final LoggedUser loggedUser;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, LoggedUser loggedUser, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.loggedUser = loggedUser;
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
        user.setPassword(passwordEncoder.encode(userRegisterBindingModel.getPassword()));
        return user;
    }




    @Override
    public void logout() {
        loggedUser.setUsername(null);
        loggedUser.setLogged(false);
    }

    @Override
    public boolean isLogged() {
        if (loggedUser.isLogged()) {
            return true;
        }
        return false;
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        UserEntity user = this.userRepository.findUserByEmail(email);

        if (user != null) {
            return user;
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
}

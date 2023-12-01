package org.softuni.bookshopbg.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.softuni.bookshopbg.model.dto.UserRegisterBindingModel;
import org.softuni.bookshopbg.model.entities.*;
import org.softuni.bookshopbg.model.enums.UserRoleEnum;
import org.softuni.bookshopbg.model.security.PasswordResetToken;
import org.softuni.bookshopbg.repositories.*;
import org.softuni.bookshopbg.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    
    private final UserShippingRepository userShippingRepository;
    private final UserPaymentRepository userPaymentRepository;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final PasswordResetTokenRepository passwordResetTokenRepository;


    public UserServiceImpl(UserPaymentRepository userPaymentRepository,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           PasswordResetTokenRepository passwordResetTokenRepository,
                           UserShippingRepository userShippingRepository) {
        this.userPaymentRepository = userPaymentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userShippingRepository = userShippingRepository;
    }
    @Override
    public boolean register(
            UserRegisterBindingModel userRegisterBindingModel) {

        userRepository.save(map(userRegisterBindingModel));
        return true;
    }


    @Override
    @Transactional
    public UserEntity createUser(UserEntity user){
      Optional<UserEntity> localUser = userRepository.findUserEntitiesByUsername(user.getUsername());

      if (localUser.isPresent()) {
          LOG.info("User with username {} already exists. Nothing will be done. ", user.getUsername());
        } else {


          if (userRepository.count() == 0) {
              user.getRoles().add(roleRepository.findByRole(UserRoleEnum.ADMIN));
              user.getRoles().add(roleRepository.findByRole(UserRoleEnum.USER));
          } else {
              user.getRoles().add(roleRepository.findByRole(UserRoleEnum.USER));
          }


          ShoppingCart shoppingCart = new ShoppingCart();
          shoppingCart.setUser(user);
          user.setShoppingCart(shoppingCart);

          user.setUserShippingList(new ArrayList<>());
          user.setUserPaymentList(new ArrayList<UserPayment>());


          return userRepository.save(user);
      }
        return null;
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        passwordResetTokenRepository.deleteAllExpiredSince(new Date());
        return passwordResetTokenRepository.findByToken(token);

    }

    @Override
    public UserEntity findById(Long id) {
        return userRepository.findById(id).get();
    }

    public UserEntity map(UserRegisterBindingModel userRegisterBindingModel) {
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
        Optional<UserEntity> user = this.userRepository.findUserEntitiesByEmail(email);

        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    @Override
    public UserEntity findUserByUsername(String username) {
        if (this.userRepository.findUserEntitiesByUsername(username).isEmpty()){
            return null;
        }
        return this.userRepository.findUserEntitiesByUsername(username).get();
    }
    @Override
    public void createPasswordResetTokenForUser(UserEntity user, String token) {
        passwordResetTokenRepository.deleteAllExpiredSince(new Date());
        PasswordResetToken tokenRepositoryByTokenToken = passwordResetTokenRepository.findByUser(user);
        if (tokenRepositoryByTokenToken == null) {
            PasswordResetToken myToken = new PasswordResetToken(token, user);
            passwordResetTokenRepository.save(myToken);
        }
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        Optional<UserEntity> user = this.userRepository.findUserEntitiesByUsername(username);

        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, UserEntity user) {
            UserPayment userPaymentExists = userPaymentRepository.findUserPaymentByCardName(userPayment.getCardName());
            if(userPaymentExists != null){
                UserBilling userBillingExists = userPaymentExists.getUserBilling();
                userBillingExists.setUserBillingName(userBilling.getUserBillingName());
                userBillingExists.setUserBillingStreet1(userBilling.getUserBillingStreet1());
                userBillingExists.setUserBillingStreet2(userBilling.getUserBillingStreet2());
                userBillingExists.setUserBillingCity(userBilling.getUserBillingCity());
                userBillingExists.setUserBillingState(userBilling.getUserBillingState());
                userBillingExists.setUserBillingCountry(userBilling.getUserBillingCountry());
                userBillingExists.setUserBillingZipcode(userBilling.getUserBillingZipcode());


                userPaymentExists.setUserBilling(userBilling);
                userPaymentExists.setDefaultPayment(true);
                userBilling.setUserPayment(userPaymentExists);
                userPaymentRepository.save(userPaymentExists);

            } else {
                userPayment.setUser(user);
                userPayment.setUserBilling(userBilling);
                userPayment.setDefaultPayment(true);
                userBilling.setUserPayment(userPayment);
                user.getUserPaymentList().add(userPayment);
                userRepository.save(user);
            }

    }

    @Override
    public void updateUserShipping(UserShipping userShipping, UserEntity user){
        UserShipping userShippingExists = userShippingRepository.findUserShippingByUserShippingName(userShipping.getUserShippingName());
        if(userShippingExists != null){
            userShippingExists.setUserShippingName(userShipping.getUserShippingName());
            userShippingExists.setUserShippingStreet1(userShipping.getUserShippingStreet1());
            userShippingExists.setUserShippingStreet2(userShipping.getUserShippingStreet2());
            userShippingExists.setUserShippingCity(userShipping.getUserShippingCity());
            userShippingExists.setUserShippingState(userShipping.getUserShippingState());
            userShippingExists.setUserShippingCountry(userShipping.getUserShippingCountry());
            userShippingExists.setUserShippingZipcode(userShipping.getUserShippingZipcode());

            userShippingRepository.save(userShippingExists);
            return;
        }

        userShipping.setUser(user);
        userShipping.setUserShippingDefault(true);
        userShippingRepository.save(userShipping);
        user.getUserShippingList().add(userShipping);
        userRepository.save(user);
    }

    @Override
    public void setUserDefaultPayment(Long userPaymentId, UserEntity user) {
        List<UserPayment> userPaymentList = (List<UserPayment>) userPaymentRepository.findAll();

        for (UserPayment userPayment : userPaymentList) {
            if(userPayment.getId() == userPaymentId) {
                userPayment.setDefaultPayment(true);
                userPaymentRepository.save(userPayment);
            } else {
                userPayment.setDefaultPayment(false);
                userPaymentRepository.save(userPayment);
            }
        }
    }

    @Override
    public void setUserDefaultShipping(Long userShippingId, UserEntity user) {
        List<UserShipping> userShippingList = (List<UserShipping>) userShippingRepository.findAll();

        for (UserShipping userShipping : userShippingList) {
            if(userShipping.getId() == userShippingId) {
                userShipping.setUserShippingDefault(true);
                userShippingRepository.save(userShipping);
            } else {
                userShipping.setUserShippingDefault(false);
                userShippingRepository.save(userShipping);
            }
        }
    }
}

package org.softuni.bookshopbg.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.softuni.bookshopbg.model.dto.UserRegisterBindingModel;
import org.softuni.bookshopbg.model.entities.*;
import org.softuni.bookshopbg.model.security.PasswordResetToken;
import org.softuni.bookshopbg.model.security.UserRoleEntity;
import org.softuni.bookshopbg.model.enums.UserRoleEnum;
import org.softuni.bookshopbg.repositories.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserServiceImplTest {
    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserPaymentRepository mockUserPaymentRepository;

    @Mock
    private UserShippingRepository mockUserShippingRepository;
    @Mock
    private RoleRepository mockRoleRepository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private UserServiceImpl userServiceToTest;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private UserEntity mockuser;

    @Mock
    private PasswordResetToken mockPasswordResetToken;

    @BeforeEach
    void setUp() {
        userServiceToTest = new UserServiceImpl(mockUserPaymentRepository,
                mockUserRepository,
                mockRoleRepository,
                mockPasswordEncoder,
                passwordResetTokenRepository,
                mockUserShippingRepository);

        mockuser.setUsername("ivan");
        mockuser.setPassword("1234");

        mockPasswordResetToken.setToken("1234");
        mockPasswordResetToken.setUser(mockuser);
        mockPasswordResetToken.setExpiryDate(null);
        mockPasswordResetToken.setId(1L);
    }

    @AfterEach
    void tearDown() {
        userServiceToTest = null;
    }


    @Test
    void createUserWithAdminRoleTest() {
        //Arrange
        when(mockUserRepository.findUserEntitiesByUsername(mockuser.getUsername())).thenReturn(Optional.empty());
        when(mockUserRepository.count()).thenReturn(0L);
        when(mockUserRepository.save(any())).thenReturn(mockuser);
        //Act
        UserEntity result = userServiceToTest.createUser(mockuser);
        //Assert
        assertEquals(mockuser, result);
    }

    @Test
    void  createUserWithExistingUser(){
        //Arrange
        when(mockUserRepository.findUserEntitiesByUsername(mockuser.getUsername())).thenReturn(Optional.of(mockuser));
        when(mockUserRepository.count()).thenReturn(1L);
        when(mockUserRepository.save(any())).thenReturn(mockuser);
        //Act
        UserEntity result = userServiceToTest.createUser(mockuser);
        //Assert
        assertNull(result);
    }

    @Test
    void createUserWIthUserRoleTest(){
        //Arrange
        when(mockUserRepository.findUserEntitiesByUsername(mockuser.getUsername())).thenReturn(Optional.empty());
        when(mockUserRepository.count()).thenReturn(1L);
        when(mockUserRepository.save(any())).thenReturn(mockuser);
        //Act
        UserEntity result = userServiceToTest.createUser(mockuser);
        //Assert
        assertEquals(mockuser, result);
    }

    @Test
    void  getPasswordResetTokenTest(){
        //Arrange
        when(passwordResetTokenRepository.findByToken(any())).thenReturn(mockPasswordResetToken);
        //Act
        userServiceToTest.getPasswordResetToken( "1234");
        //Assert
        verify(passwordResetTokenRepository, times(1)).findByToken(any());
    }

    @Test
    void  createPasswordResetTokenZForUserTest(){
        //Arrange
        when(passwordResetTokenRepository.save(any())).thenReturn(mockPasswordResetToken);
        //Act
        userServiceToTest.createPasswordResetTokenForUser(mockuser, "1234");
        //Assert
        verify(passwordResetTokenRepository, times(1)).save(any());
    }

    @Test
    void updateUserBillingTest() {
        //Arrange
        UserEntity user = createUser();
        UserBilling userBilling = new UserBilling();
        when(mockUserPaymentRepository.findUserPaymentByCardName(any())).thenReturn(user.getUserPaymentList().get(0));
        when(mockUserPaymentRepository.save(any())).thenReturn(user.getUserPaymentList().get(0));
        when(mockUserRepository.save(any())).thenReturn(user);
        //Act
        userServiceToTest.updateUserBilling(userBilling, user.getUserPaymentList().get(0), user);
        //Assert
        verify(mockUserRepository, times(1)).save(any());
        assertEquals(user.getUserPaymentList().get(0).getUserBilling(), userBilling);
    }

    @Test
    void updateUserBillingTestNull() {
        //Arrange
       UserEntity user = createUser();
        UserBilling userBilling = new UserBilling();
        UserPayment userPayment = new UserPayment();
        user.setUserPaymentList(new ArrayList<>());
        when(mockUserPaymentRepository.findUserPaymentByCardName(any())).thenReturn(null);
        when(mockUserRepository.save(user)).thenReturn(user);
        //Act
        userServiceToTest.updateUserBilling(userBilling, userPayment, user);
        //Assert
        assertEquals(user.getUserPaymentList().get(0).getUserBilling(), userBilling);
        verify(mockUserRepository,times(1)).save(user);
    }

    @Test
    void updateUserShippingTest() {
        //Arrange
        UserEntity user = createUser();
        UserShipping userShipping = createUserShipping();
        user.setUserShippingList(List.of(userShipping));
        userShipping.setUser(user);
        when(mockUserShippingRepository.save(any())).thenReturn(userShipping);
        when(mockUserShippingRepository.findUserShippingByUserShippingName(userShipping.getUserShippingName())).thenReturn(userShipping);
        //Act
        userServiceToTest.updateUserShipping(userShipping, user);
        //Assert
        verify(mockUserShippingRepository, times(1)).save(any());
    }

    @Test
    void updateUserShippingTestNull() {
        //Arrange
        UserEntity user = createUser();
        UserShipping userShipping = new UserShipping();
        when(mockUserShippingRepository.save(any())).thenReturn(userShipping);
        when(mockUserShippingRepository.findUserShippingByUserShippingName(userShipping.getUserShippingName())).thenReturn(null);
        when(mockUserRepository.save(user)).thenReturn(user);
        //Act
        userServiceToTest.updateUserShipping(userShipping, user);
        //Assert
        verify(mockUserShippingRepository, times(1)).save(any());
        verify(mockUserRepository,times(1)).save(user);
    }

    @Test
    void setDefaultPaymentTest() {
        // Arrange
        UserEntity user = createUser();
        UserPayment userPayment = new UserPayment();
        userPayment.setDefaultPayment(true);
        userPayment.setUser(user);
        when(mockUserPaymentRepository.findAll()).thenReturn(List.of(userPayment));
        when(mockUserPaymentRepository.save(any())).thenReturn(userPayment);
        // Act
        userServiceToTest.setUserDefaultPayment(userPayment.getId(), user);
        // Assert
        verify(mockUserPaymentRepository, times(1)).save(any());
    }
    @Test
    void setDefaultPaymentFalseTest() {
        // Arrange
        UserEntity user = createUser();
        UserPayment userPayment = new UserPayment();
        userPayment.setDefaultPayment(false);
        userPayment.setUser(user);
        when(mockUserPaymentRepository.findAll()).thenReturn(List.of(userPayment));
        when(mockUserPaymentRepository.save(any())).thenReturn(userPayment);
        // Act
        userServiceToTest.setUserDefaultPayment(2L, user);
        // Assert
        verify(mockUserPaymentRepository, times(1)).save(any());
    }

    @Test
    void  setUserDefaultShippingTest(){
        // Arrange
        UserEntity user = createUser();
        UserShipping userShipping = new UserShipping();
        userShipping.setUserShippingDefault(true);
        userShipping.setUser(user);
        when(mockUserShippingRepository.findAll()).thenReturn(List.of(userShipping));
        when(mockUserShippingRepository.save(any())).thenReturn(userShipping);
        // Act
        userServiceToTest.setUserDefaultShipping(userShipping.getId(), user);
        // Assert
        verify(mockUserShippingRepository, times(1)).save(any());
    }

    @Test
    void  setUserDefaultShippingFalseTest(){
        // Arrange
        UserEntity user = createUser();
        UserShipping userShipping = new UserShipping();
        userShipping.setUserShippingDefault(false);
        userShipping.setUser(user);
        when(mockUserShippingRepository.findAll()).thenReturn(List.of(userShipping));
        when(mockUserShippingRepository.save(any())).thenReturn(userShipping);
        // Act
        userServiceToTest.setUserDefaultShipping(2L, user);
        // Assert
        verify(mockUserShippingRepository, times(1)).save(any());
    }
    @Test
    void registerTest() {
        // Arrange
        UserEntity user = createUser();
        user.setPassword(mockPasswordEncoder.encode(user.getPassword()));
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        when(mockUserRepository.save(any())).thenReturn(user);
        // Act
        boolean result = userServiceToTest.register(userRegisterBindingModel);
        // Assert
        assertTrue(result);

    }
    @Test
    void mapTest(){
        // Arrange
        UserRegisterBindingModel userRegisterBindingModel = createUserRegisterBindingModel();
        when(mockUserRepository.count()).thenReturn(0L);
        when(mockRoleRepository.findByRole(UserRoleEnum.USER)).thenReturn(new UserRoleEntity().setRole(UserRoleEnum.USER));
        when(mockPasswordEncoder.encode(any())).thenReturn("1234");
        // Act
        UserEntity result = userServiceToTest.map(userRegisterBindingModel);
        // Assert
        assertEquals(userRegisterBindingModel.getFirstName(), result.getFirstName());
    }

    @Test
    void saveTest(){
        // Arrange
        UserEntity user = createUser();
        when(mockUserRepository.save(any())).thenReturn(user);
        // Act
        UserEntity result = userServiceToTest.save(user);
        // Assert
        assertEquals(user, result);
    }
    @Test
    void mapTestAdmin(){
        // Arrange
        UserRegisterBindingModel userRegisterBindingModel = createUserRegisterBindingModel();
        when(mockUserRepository.count()).thenReturn(1L);
        when(mockRoleRepository.findByRole(UserRoleEnum.ADMIN)).thenReturn(new UserRoleEntity().setRole(UserRoleEnum.ADMIN));
        when(mockPasswordEncoder.encode(any())).thenReturn("1234");
        // Act
        UserEntity result = userServiceToTest.map(userRegisterBindingModel);
        // Assert
        assertEquals(userRegisterBindingModel.getFirstName(), result.getFirstName());
    }

    @Test
    void findUserByEmailTest() {
        // Arrange
        String email = "ivan@mail.com";
        String emailNull = "";
        UserEntity user = createUser();
        when(mockUserRepository.findUserEntitiesByEmail(email)).thenReturn(java.util.Optional.of(user));
        // Act
        UserEntity result = userServiceToTest.findUserByEmail(email);
        UserEntity resultNull = userServiceToTest.findUserByEmail(emailNull);
        // Assert
        assertEquals(user, result);
        assertNull(resultNull);
    }

    @Test
    void findUserByEmailTestNull() {
        // Arrange
        String email = "";
        when(mockUserRepository.findUserEntitiesByEmail(email)).thenReturn(Optional.empty());
        // Act
        UserEntity result = userServiceToTest.findUserByEmail(email);
        // Assert
        assertNull(result);
    }

    @Test
    void findUserByUsernameTest() {
        // Arrange
        String username = "ivan";
        UserEntity user = createUser();
        when(mockUserRepository.findUserEntitiesByUsername(username)).thenReturn(java.util.Optional.of(user));
        // Act
        UserEntity result = userServiceToTest.findUserByUsername(username);
        // Assert
        assertEquals(user, result);
    }

    @Test
    void checkCredentialsTest() {
        // Arrange
        String username = "ivan";
        String password = "1234";
        UserEntity user = createUser();
        user.setPassword(mockPasswordEncoder.encode(password));
        mockUserRepository.save(user);
        when(mockUserRepository.findUserEntitiesByUsername(username)).thenReturn(java.util.Optional.of(user));
        when(mockPasswordEncoder.matches(password, user.getPassword())).thenReturn(true);
        // Act
        boolean result = userServiceToTest.checkCredentials(username, password);
        // Assert
        assertTrue(result);
    }

    @Test
    void checkCredentialsTestFalse() {
        // Arrange
        String username = "ivan";
        String password = "1234";
        UserEntity user = createUser();
        user.setPassword(mockPasswordEncoder.encode(password));
        mockUserRepository.save(user);
        when(mockUserRepository.findUserEntitiesByUsername(username)).thenReturn(java.util.Optional.of(user));
        when(mockPasswordEncoder.matches(password, user.getPassword())).thenReturn(false);
        // Act
        boolean result = userServiceToTest.checkCredentials(username, password);
        // Assert
        assertFalse(result);
    }

    @Test
    void  findByIdTest(){
        // Arrange
        Long id = 1L;
        UserEntity user = createUser();
        when(mockUserRepository.findById(id)).thenReturn(java.util.Optional.of(user));
        // Act
        UserEntity result = userServiceToTest.findById(id);
        // Assert
        assertEquals(user, result);
    }

    private UserRegisterBindingModel createUserRegisterBindingModel() {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setFirstName("Ivan");
        userRegisterBindingModel.setLastName("Ivanov");
        userRegisterBindingModel.setEmail("ivan@mail.com");
        userRegisterBindingModel.setUsername("ivan");
        UserRegisterBindingModel.setPassword("1234");
        userRegisterBindingModel.setConfirmPassword("1234");
        return userRegisterBindingModel;
    }

    private UserEntity createUser() {
        UserEntity user = new UserEntity();
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setEmail("ivan@mail.com");
        user.setUsername("ivan");
        user.setPassword("1234");
        user.setActive(true);
        UserBilling userBilling = new UserBilling();
        Payment payment = new Payment();
        payment.setCardName("Ivan");
        UserPayment userPayment = new UserPayment();
        userPayment.setDefaultPayment(true);
        userPayment.setUserBilling(userBilling);
        user.setUserPaymentList(List.of(userPayment));
        return user;
    }
    private UserShipping createUserShipping() {
        UserShipping userShipping = new UserShipping();
        userShipping.setUserShippingName("ivan");
        userShipping.setId(1L);
        userShipping.setUserShippingDefault(true);
        return userShipping;
    }
}
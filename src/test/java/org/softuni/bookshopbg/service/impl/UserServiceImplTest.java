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

        when(mockUserRepository.findByUsername(mockuser.getUsername())).thenReturn(Optional.empty());
        when(mockUserRepository.count()).thenReturn(0L);
        when(mockUserRepository.save(any())).thenReturn(mockuser);

        UserEntity result = userServiceToTest.createUser(mockuser);

        assertEquals(mockuser, result);

    }

    @Test
    void  createUserWithExistingUser(){

        when(mockUserRepository.findByUsername(mockuser.getUsername())).thenReturn(Optional.of(mockuser));

        when(mockUserRepository.count()).thenReturn(1L);
        when(mockUserRepository.save(any())).thenReturn(mockuser);
        UserEntity result = userServiceToTest.createUser(mockuser);

        assertNull(result);
    }

    @Test
    void createUserWIthUserRoleTest(){

        when(mockUserRepository.findByUsername(mockuser.getUsername())).thenReturn(Optional.empty());
        when(mockUserRepository.count()).thenReturn(1L);
        when(mockUserRepository.save(any())).thenReturn(mockuser);

        UserEntity result = userServiceToTest.createUser(mockuser);

        assertEquals(mockuser, result);
    }

    @Test
    void  getPasswordResetTokenTest(){
        when(passwordResetTokenRepository.findByToken(any())).thenReturn(mockPasswordResetToken);

        userServiceToTest.getPasswordResetToken( "1234");

        verify(passwordResetTokenRepository, times(1)).findByToken(any());
    }

    @Test
    void  createPasswordResetTokenZForUserTest(){


        when(passwordResetTokenRepository.save(any())).thenReturn(mockPasswordResetToken);

        userServiceToTest.createPasswordResetTokenForUser(mockuser, "1234");

        verify(passwordResetTokenRepository, times(1)).save(any());
    }

    @Test
    void updateUserBillingTest() {
        UserEntity user = createUser();
        UserBilling userBilling = new UserBilling();
        Payment payment = new Payment();
        payment.setCardName("Ivan");
        UserPayment userPayment = new UserPayment();
        userPayment.setDefaultPayment(true);
        userPayment.setUserBilling(userBilling);
        user.setUserPaymentList(List.of(userPayment));


        when(mockUserPaymentRepository.findUserPaymentByCardName(any())).thenReturn(user.getUserPaymentList().get(0));
        when(mockUserPaymentRepository.save(any())).thenReturn(user.getUserPaymentList().get(0));

        userServiceToTest.updateUserBilling(userBilling, user.getUserPaymentList().get(0), user);

        verify(mockUserPaymentRepository, times(1)).save(any());
        assertEquals(user.getUserPaymentList().get(0).getUserBilling(), userBilling);
    }

    @Test
    void updateUserBillingTestNull() {
        UserEntity user = createUser();
        UserBilling userBilling = new UserBilling();
        Payment payment = new Payment();
        payment.setCardName("Ivan");
        UserPayment userPayment = new UserPayment();


        when(mockUserPaymentRepository.findUserPaymentByCardName(any())).thenReturn(userPayment);

        when(mockUserRepository.save(user)).thenReturn(user);

        userServiceToTest.updateUserBilling(userBilling, userPayment, user);

      assertEquals(user.getUserPaymentList().get(0).getUserBilling(), userBilling);

        verify(mockUserRepository,times(1)).save(user);
    }

    @Test
    void updateUserShippingTest() {
        UserEntity user = createUser();
        UserShipping userShipping = new UserShipping();
        userShipping.setUserShippingName("ivan");

        userShipping.setId(1L);

        userShipping.setUserShippingDefault(true);

        user.setUserShippingList(List.of(userShipping));
        userShipping.setUser(user);

        when(mockUserShippingRepository.save(any())).thenReturn(userShipping);
        when(mockUserShippingRepository.findUserShippingByUserShippingName(userShipping.getUserShippingName())).thenReturn(userShipping);

        userServiceToTest.updateUserShipping(userShipping, user);

        verify(mockUserShippingRepository, times(1)).save(any());
    }

    @Test
    void updateUserShippingTestNull() {
        UserEntity user = createUser();
        UserShipping userShipping = new UserShipping();

        when(mockUserShippingRepository.save(any())).thenReturn(userShipping);
        when(mockUserShippingRepository.findUserShippingByUserShippingName(userShipping.getUserShippingName())).thenReturn(null);

        when(mockUserRepository.save(user)).thenReturn(user);

        userServiceToTest.updateUserShipping(userShipping, user);

        verify(mockUserShippingRepository, times(1)).save(any());

        verify(mockUserRepository,times(1)).save(user);
    }

    @Test
    void setDefaultPaymentTest() {
        UserEntity user = createUser();
        UserPayment userPayment = new UserPayment();
        userPayment.setDefaultPayment(true);
        userPayment.setUser(user);

        when(mockUserPaymentRepository.findAll()).thenReturn(List.of(userPayment));
        when(mockUserPaymentRepository.save(any())).thenReturn(userPayment);

        userServiceToTest.setUserDefaultPayment(userPayment.getId(), user);

        verify(mockUserPaymentRepository, times(1)).save(any());
    }
    @Test
    void setDefaultPaymentFalseTest() {
        UserEntity user = createUser();
        UserPayment userPayment = new UserPayment();
        userPayment.setDefaultPayment(false);
        userPayment.setUser(user);

        when(mockUserPaymentRepository.findAll()).thenReturn(List.of(userPayment));
        when(mockUserPaymentRepository.save(any())).thenReturn(userPayment);

        userServiceToTest.setUserDefaultPayment(2L, user);

        verify(mockUserPaymentRepository, times(1)).save(any());
    }

    @Test
    void  setUserDefaultShippingTest(){
        UserEntity user = createUser();
        UserShipping userShipping = new UserShipping();
        userShipping.setUserShippingDefault(true);
        userShipping.setUser(user);

        when(mockUserShippingRepository.findAll()).thenReturn(List.of(userShipping));
        when(mockUserShippingRepository.save(any())).thenReturn(userShipping);

        userServiceToTest.setUserDefaultShipping(userShipping.getId(), user);

        verify(mockUserShippingRepository, times(1)).save(any());
    }

    @Test
    void  setUserDefaultShippingFalseTest(){
        UserEntity user = createUser();
        UserShipping userShipping = new UserShipping();
        userShipping.setUserShippingDefault(false);
        userShipping.setUser(user);

        when(mockUserShippingRepository.findAll()).thenReturn(List.of(userShipping));
        when(mockUserShippingRepository.save(any())).thenReturn(userShipping);

        userServiceToTest.setUserDefaultShipping(2L, user);

        verify(mockUserShippingRepository, times(1)).save(any());
    }
    @Test
    void registerTest() {
        UserEntity user = createUser();
        user.setPassword(mockPasswordEncoder.encode(user.getPassword()));

        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();


        when(mockUserRepository.save(any())).thenReturn(user);

        boolean result = userServiceToTest.register(userRegisterBindingModel);

        assertTrue(result);

    }
    @Test
    void mapTest(){
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setFirstName("Ivan");
        userRegisterBindingModel.setLastName("Ivanov");
        userRegisterBindingModel.setEmail("ivan@mail.com");
        userRegisterBindingModel.setUsername("ivan");
        UserRegisterBindingModel.setPassword("1234");
        userRegisterBindingModel.setConfirmPassword("1234");


        when(mockUserRepository.count()).thenReturn(0L);
        when(mockRoleRepository.findByRole(UserRoleEnum.USER)).thenReturn(new UserRoleEntity().setRole(UserRoleEnum.USER));
        when(mockPasswordEncoder.encode(any())).thenReturn("1234");

        UserEntity result = userServiceToTest.map(userRegisterBindingModel);

        assertEquals(userRegisterBindingModel.getFirstName(), result.getFirstName());
    }

    @Test
    void saveTest(){
        UserEntity user = createUser();

        when(mockUserRepository.save(any())).thenReturn(user);

        UserEntity result = userServiceToTest.save(user);

        assertEquals(user, result);
    }
    @Test
    void mapTestAdmin(){
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setFirstName("Ivan");
        userRegisterBindingModel.setLastName("Ivanov");
        userRegisterBindingModel.setEmail("ivan@mail.com");
        userRegisterBindingModel.setUsername("ivan");
        userRegisterBindingModel.setPassword("1234");
        userRegisterBindingModel.setConfirmPassword("1234");

        when(mockUserRepository.count()).thenReturn(1L);
        when(mockRoleRepository.findByRole(UserRoleEnum.ADMIN)).thenReturn(new UserRoleEntity().setRole(UserRoleEnum.ADMIN));
        when(mockPasswordEncoder.encode(any())).thenReturn("1234");

        UserEntity result = userServiceToTest.map(userRegisterBindingModel);

        assertEquals(userRegisterBindingModel.getFirstName(), result.getFirstName());
    }

    @Test
    void findUserByEmailTest() {
        String email = "ivan@mail.com";

        String emailNull = "";

        UserEntity user = createUser();

        when(mockUserRepository.findByEmail(email)).thenReturn(java.util.Optional.of(user));

        UserEntity result = userServiceToTest.findUserByEmail(email);

        assertEquals(user, result);

        UserEntity resultNull = userServiceToTest.findUserByEmail(emailNull);

        assertNull(resultNull);
    }

    @Test
    void findUserByEmailTestNull() {
        String email = "";

        when(mockUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        UserEntity result = userServiceToTest.findUserByEmail(email);

        assertNull(result);

    }

    @Test
    void findUserByUsernameTest() {
        String username = "ivan";

        UserEntity user = createUser();

        when(mockUserRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));

        UserEntity result = userServiceToTest.findUserByUsername(username);

        assertEquals(user, result);
    }



    @Test
    void checkCredentialsTest() {
        String username = "ivan";
        String password = "1234";

        UserEntity user = createUser();
        user.setPassword(mockPasswordEncoder.encode(password));
        mockUserRepository.save(user);

        when(mockUserRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));

        when(mockPasswordEncoder.matches(password, user.getPassword())).thenReturn(true);

        boolean result = userServiceToTest.checkCredentials(username, password);

        assertTrue(result);
    }

    @Test
    void checkCredentialsTestFalse() {
        String username = "ivan";
        String password = "1234";

        UserEntity user = createUser();
        user.setPassword(mockPasswordEncoder.encode(password));
        mockUserRepository.save(user);

        when(mockUserRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));

        when(mockPasswordEncoder.matches(password, user.getPassword())).thenReturn(false);

        boolean result = userServiceToTest.checkCredentials(username, password);

        assertFalse(result);
    }
    @Test
    void  findByIdTest(){
        Long id = 1L;

        UserEntity user = createUser();

        when(mockUserRepository.findById(id)).thenReturn(java.util.Optional.of(user));

        UserEntity result = userServiceToTest.findById(id);

        assertEquals(user, result);
    }


    private UserEntity createUser() {
        UserEntity user = new UserEntity();
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setEmail("ivan@mail.com");
        user.setUsername("ivan");
        user.setPassword("1234");
        user.setActive(true);
        return user;
    }
}
package org.softuni.bookshopbg.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.softuni.bookshopbg.model.dto.UserRegisterBindingModel;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.model.entities.UserRoleEntity;
import org.softuni.bookshopbg.model.enums.UserRoleEnum;
import org.softuni.bookshopbg.repositories.RoleRepository;
import org.softuni.bookshopbg.repositories.UserRepository;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private RoleRepository mockRoleRepository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private UserServiceImpl userServiceToTest;

    @BeforeEach
    void setUp() {
        userServiceToTest = new UserServiceImpl(mockUserRepository, mockRoleRepository, mockPasswordEncoder);
    }

    @AfterEach
    void tearDown() {
        userServiceToTest = null;
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
        userRegisterBindingModel.setPassword("1234");
        userRegisterBindingModel.setConfirmPassword("1234");


        when(mockUserRepository.count()).thenReturn(0L);
        when(mockRoleRepository.findByRole(UserRoleEnum.USER)).thenReturn(new UserRoleEntity().setRole(UserRoleEnum.USER));
        when(mockPasswordEncoder.encode(any())).thenReturn("1234");

        UserEntity result = userServiceToTest.map(userRegisterBindingModel);

        assertEquals(userRegisterBindingModel.getFirstName(), result.getFirstName());
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

        Optional<UserEntity> result = userServiceToTest.findUserByUsername(username);

        assertEquals(user, result.get());
    }

    @Test
    void findUserByUsernameTestNull() {
        String username = "ivan";

        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<UserEntity> result = userServiceToTest.findUserByUsername(username);

        assertEquals(Optional.empty(), result);
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
    void saveTest() {
        UserEntity user = createUser();

        when(mockUserRepository.save(user)).thenReturn(user);

        userServiceToTest.save(user);

        verify(mockUserRepository,times(1)).save(user);

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
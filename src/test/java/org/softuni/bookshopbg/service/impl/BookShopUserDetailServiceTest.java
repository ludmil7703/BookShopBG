package org.softuni.bookshopbg.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.model.entities.UserRoleEntity;
import org.softuni.bookshopbg.model.enums.UserRoleEnum;
import org.softuni.bookshopbg.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookShopUserDetailServiceTest {

    private BookShopUserDetailService serviceToTest;

    @Mock
    private UserRepository mockUserRepository;


    @BeforeEach
     void setUp() {
        serviceToTest = new BookShopUserDetailService(mockUserRepository);
    }

    @Test
    void testUserNotFound() {
         Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> serviceToTest.loadUserByUsername("pesho"));
        }
        @Test
    void testUserNotFoundExceptionMessage() {
        UsernameNotFoundException exception = Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> serviceToTest.loadUserByUsername("pesho"));

        Assertions.assertNotNull(exception);

        Assertions.assertEquals("User pesho not found!", exception.getMessage());
    }

    @Test
    void testExistingUser() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("pesho");
        userEntity.setPassword("12345");
        userEntity.setActive(true);
        userEntity.setRoles(List.of(new UserRoleEntity().setRole(UserRoleEnum.USER),
                new UserRoleEntity().setRole(UserRoleEnum.ADMIN)));
        when(mockUserRepository.findByUsername("pesho"))
                .thenReturn(Optional.of(userEntity));

        // Act
        var userDetails = serviceToTest.loadUserByUsername("pesho");

        // Assert
        Assertions.assertNotNull(userDetails);
        Assertions.assertTrue(containsAuthority(userDetails, "ROLE_" + UserRoleEnum.USER));
        Assertions.assertTrue(containsAuthority(userDetails, "ROLE_" + UserRoleEnum.ADMIN));
        Assertions.assertEquals(2, userDetails.getAuthorities().size(), "Roles count is not correct!");
        Assertions.assertEquals(userEntity.getUsername(), userDetails.getUsername(), "Username is not correct!" );
        Assertions.assertEquals(userEntity.getPassword(), userDetails.getPassword(), "Password is not correct!" );

        }
    private boolean containsAuthority(UserDetails userDetails, String expectedAuthority) {
        return userDetails.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(expectedAuthority));

    }


}

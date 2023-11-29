package org.softuni.bookshopbg.service.impl;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.model.entities.UserShipping;
import org.softuni.bookshopbg.repositories.UserShippingRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserShippingServiceImplTest {

    @Mock
    private UserShippingRepository mockUserShippingRepository;

    @Mock
    private UserShippingServiceImpl userShippingServiceToTest;

    @BeforeEach
    void setUp() {
        userShippingServiceToTest = new UserShippingServiceImpl(mockUserShippingRepository);
    }

    @AfterEach
    void tearDown() {
        userShippingServiceToTest = null;
    }

    @Test
    void findById() {
        // Arrange
        Optional<UserShipping> expectedResult = createUserShipping();
        when(mockUserShippingRepository.findById(1L)).thenReturn(expectedResult);
        // Act
        Optional<UserShipping> actualResult = userShippingServiceToTest.findById(1L);
        // Assert
        assertEquals(expectedResult,actualResult);
    }

    @Test
    void deleteById() {
        // Arrange
        Optional<UserShipping> userShippingToDelete = createUserShipping();
        assertTrue(userShippingToDelete.isPresent());
        when(mockUserShippingRepository.findById(1L)).thenReturn(userShippingToDelete);
        doNothing().when(mockUserShippingRepository).deleteById(userShippingToDelete.get().getId());
        // Act
        userShippingServiceToTest.deleteById(1L);
        // Assert
        verify(mockUserShippingRepository, times(1)).deleteById(1L);
    }

    private Optional<UserShipping> createUserShipping(){
        Optional<UserShipping> userShipping = Optional.of(new UserShipping());
        userShipping.get().setId(1L);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.getUserShippingList().add(userShipping.get());

        userShipping.get().setUser(userEntity);
        userShipping.get().setUserShippingName("Shipping Name");
        userShipping.get().setUserShippingCity("Shipping City");

        return userShipping;
    }
}
package org.softuni.bookshopbg.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.model.entities.UserShipping;
import org.softuni.bookshopbg.repositories.UserShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserShippingServiceImplTest {

    @Mock
    private UserShippingRepository mockUserShippingRepository;

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
        Optional<UserShipping> userShipping = createUserShipping();
        when(mockUserShippingRepository.findById(1L)).thenReturn(userShipping);

        assertEquals(userShipping, userShippingServiceToTest.findById(1L));


    }

    @Test
    void deleteById() {
        Optional<UserShipping> userShippingToDelete = createUserShipping();
        when(mockUserShippingRepository.findById(1L)).thenReturn(userShippingToDelete);
        doNothing().when(mockUserShippingRepository).deleteById(userShippingToDelete.get().getId());

        userShippingServiceToTest.deleteById(1L);

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
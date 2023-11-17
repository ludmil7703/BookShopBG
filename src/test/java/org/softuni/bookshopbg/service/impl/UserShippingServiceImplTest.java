package org.softuni.bookshopbg.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.bookshopbg.model.entities.UserShipping;
import org.softuni.bookshopbg.repositories.UserShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
        UserShipping userShipping = createUserShipping();
        when(mockUserShippingRepository.findById(1L)).thenReturn(java.util.Optional.of(userShipping));

        assertEquals(userShipping, userShippingServiceToTest.findById(1L).get());


    }

    @Test
    void deleteById() {
        UserShipping userShipping = createUserShipping();
        mockUserShippingRepository.save(userShipping);

        userShippingServiceToTest.deleteById(1L);
        assertEquals(0, mockUserShippingRepository.count());
    }

    private UserShipping createUserShipping(){
        UserShipping userShipping = new UserShipping();
        userShipping.setId(1L);
        userShipping.setUserShippingName("Shipping Name");
        userShipping.setUserShippingCity("Shipping City");
        return userShipping;
    }
}
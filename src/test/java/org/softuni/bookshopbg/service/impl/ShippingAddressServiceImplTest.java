package org.softuni.bookshopbg.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.bookshopbg.model.entities.ShippingAddress;
import org.softuni.bookshopbg.model.entities.UserShipping;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ShippingAddressServiceImplTest {
    @Mock
    private ShippingAddressServiceImpl shippingAddressServiceToTest;

    @BeforeEach
    void setUp() {
        shippingAddressServiceToTest = new ShippingAddressServiceImpl();
    }

    @AfterEach
    void tearDown() {
        shippingAddressServiceToTest = null;
    }

    @Test
    void setByUserShipping() {
        //Arrange
        ShippingAddress shippingAddress = new ShippingAddress();
        UserShipping userShipping = createUserShipping();

        //Act
        shippingAddressServiceToTest.setByUserShipping(userShipping, shippingAddress);

        //Assert
        assertEquals(userShipping.getUserShippingName(), shippingAddress.getShippingAddressName());
    }

    private UserShipping createUserShipping(){
        UserShipping userShipping = new UserShipping();
        userShipping.setUserShippingName("Test");

        return userShipping;
    }
}
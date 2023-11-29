package org.softuni.bookshopbg.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.bookshopbg.model.entities.BillingAddress;
import org.softuni.bookshopbg.model.entities.UserBilling;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class BillingAddressServiceImplTest {

    @Mock
    private BillingAddressServiceImpl billingAddressServiceToTest;


    @BeforeEach
    void setUp() {
        billingAddressServiceToTest = new BillingAddressServiceImpl();

    }

    @AfterEach
    void tearDown() {
        billingAddressServiceToTest = null;
    }

    @Test
    void testSetByUserBilling() {
        UserBilling userBilling = creatUserBilling();
        BillingAddress billingAddress = new BillingAddress();
        billingAddressServiceToTest.setByUserBilling(userBilling, billingAddress);

        assertEquals(userBilling.getUserBillingName(), billingAddress.getBillingAddressName());
        assertEquals(userBilling.getUserBillingStreet1(), billingAddress.getBillingAddressStreet1());
    }

    public UserBilling creatUserBilling(){
        UserBilling userBilling = new UserBilling();
        userBilling.setUserBillingName("Test");
        userBilling.setUserBillingStreet1("Street");

        return userBilling;
    }
}
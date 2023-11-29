package org.softuni.bookshopbg.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.bookshopbg.model.entities.Payment;
import org.softuni.bookshopbg.model.entities.UserPayment;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    private PaymentServiceImpl paymentServiceToTest;

    @BeforeEach
    void setUp() {
        paymentServiceToTest = new PaymentServiceImpl();

    }

    @AfterEach
    void tearDown() {
        paymentServiceToTest = null;
    }

    @Test
    void setByUserPaymentTest() {
        Payment payment = new Payment();

        UserPayment userPayment = createUserPayment();

        paymentServiceToTest.setByUserPayment(userPayment, payment);

        assertEquals(userPayment.getType(), payment.getType());
        assertEquals(userPayment.getHolderName(), payment.getHolderName());
    }

    private UserPayment createUserPayment(){
        UserPayment userPayment = new UserPayment();
        userPayment.setType("Test");
        userPayment.setHolderName("Test");
        userPayment.setCardNumber("Test");
        userPayment.setExpiryMonth(1);
        userPayment.setExpiryYear(2021);
        userPayment.setCvc(123);

        return userPayment;
    }
}
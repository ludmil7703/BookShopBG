package org.softuni.bookshopbg.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.bookshopbg.model.entities.UserBilling;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.model.entities.UserPayment;
import org.softuni.bookshopbg.repositories.UserPaymentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPaymentServiceImplTest {

    @Mock
    private UserPaymentRepository mockUserPaymentRepository;

    @Mock
    private UserPaymentServiceImpl userPaymentServiceTest;

    @BeforeEach
    void setUp() {
        userPaymentServiceTest = new UserPaymentServiceImpl(mockUserPaymentRepository);
    }

    @AfterEach
    void tearDown() {
        userPaymentServiceTest = null;
    }


    @Test
    void findById() {
        UserPayment expextedUserPayment = new UserPayment();
        expextedUserPayment.setId(1L);
        expextedUserPayment.setCardName("cardName");
        expextedUserPayment.setCardNumber("cardNumber");

        mockUserPaymentRepository.save(expextedUserPayment);

        when(mockUserPaymentRepository.findById(1L)).thenReturn(Optional.of(expextedUserPayment));

        UserPayment actualUserPaymentResult = userPaymentServiceTest.findById(1L);

        UserPayment nullUserPaymentResult = userPaymentServiceTest.findById(2L);

        assertEquals(expextedUserPayment, actualUserPaymentResult);
        assertNull(nullUserPaymentResult);
    }

    @Test
    void deleteById() {
        UserPayment userPayment = createTestUserPayment();

        when(mockUserPaymentRepository.findById(1L)).thenReturn(Optional.of(userPayment));

        userPaymentServiceTest.deleteById(1L);

        verify(mockUserPaymentRepository, times(1)).deleteById(1L);

    }

    public UserPayment createTestUserPayment() {
        UserPayment userPayment = new UserPayment();
        userPayment.setId(1L);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("username");
        userEntity.getUserPaymentList().add(userPayment);

        UserBilling userBilling = new UserBilling();
        userBilling.setId(1L);
        userBilling.setUserPayment(userPayment);
        userPayment.setUserBilling(userBilling);
        userPayment.setUser(userEntity);
        userPayment.setCardName("cardName");
        userPayment.setCardNumber("cardNumber");
        return userPayment;
    }
}
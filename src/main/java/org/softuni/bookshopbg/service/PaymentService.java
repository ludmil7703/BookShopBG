package org.softuni.bookshopbg.service;


import org.softuni.bookshopbg.model.entities.Payment;
import org.softuni.bookshopbg.model.entities.UserPayment;

public interface PaymentService {
	Payment setByUserPayment(UserPayment userPayment, Payment payment);
}

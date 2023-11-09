package org.softuni.bookshopbg.service.impl;


import org.softuni.bookshopbg.model.entities.Payment;
import org.softuni.bookshopbg.model.entities.UserPayment;
import org.softuni.bookshopbg.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
	
	public Payment setByUserPayment(UserPayment userPayment, Payment payment) {
		
		payment.setType(userPayment.getType());
		payment.setHolderName(userPayment.getHolderName());
		payment.setCardNumber(userPayment.getCardNumber());
		payment.setExpiryMonth(userPayment.getExpiryMonth());
		payment.setExpiryYear(userPayment.getExpiryYear());
		payment.setCvc(userPayment.getCvc());
		
		return payment;
	}

}

package org.softuni.bookshopbg.service;

import org.softuni.bookshopbg.model.entities.UserPayment;

public interface UserPaymentService {
	UserPayment findById(Long id);
	
	void deleteById(Long id);



}

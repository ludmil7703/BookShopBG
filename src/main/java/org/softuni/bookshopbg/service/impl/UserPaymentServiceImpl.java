package org.softuni.bookshopbg.service.impl;


import org.softuni.bookshopbg.model.entities.UserPayment;
import org.softuni.bookshopbg.repositories.UserPaymentRepository;
import org.softuni.bookshopbg.service.UserPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserPaymentServiceImpl implements UserPaymentService {

	@Autowired
	private UserPaymentRepository userPaymentRepository;
		
	public UserPayment findById(Long id) {
		return userPaymentRepository.findById(id).orElse(null);
	}
	
	public void deleteById(Long id) {
		userPaymentRepository.deleteById(id);
	}
} 

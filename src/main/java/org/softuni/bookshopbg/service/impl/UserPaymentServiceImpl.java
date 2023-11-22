package org.softuni.bookshopbg.service.impl;


import org.softuni.bookshopbg.model.entities.UserPayment;
import org.softuni.bookshopbg.repositories.UserPaymentRepository;
import org.softuni.bookshopbg.service.UserPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserPaymentServiceImpl implements UserPaymentService {


	private UserPaymentRepository userPaymentRepository;

	public UserPaymentServiceImpl(UserPaymentRepository userPaymentRepository) {
		this.userPaymentRepository = userPaymentRepository;
	}

	public UserPayment findById(Long id) {
		return userPaymentRepository.findById(id).orElse(null);
	}
	
	public void deleteById(Long id) {
		UserPayment userPaymentToDelete = findById(id);
		userPaymentToDelete.getUser().getUserPaymentList().remove(userPaymentToDelete);
		userPaymentToDelete.setUser(null);
		userPaymentToDelete.getUserBilling().setUserPayment(null);
		userPaymentToDelete.setUserBilling(null);
		userPaymentRepository.deleteById(id);
	}
} 

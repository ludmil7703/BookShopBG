package org.softuni.bookshopbg.service.impl;


import org.softuni.bookshopbg.model.entities.UserShipping;
import org.softuni.bookshopbg.repositories.UserShippingRepository;
import org.softuni.bookshopbg.service.UserShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserShippingServiceImpl implements UserShippingService {
	
	@Autowired
	private UserShippingRepository userShippingRepository;
	
	
	public Optional<UserShipping> findById(Long id) {
		return userShippingRepository.findById(id);
	}
	
	public void deleteById(Long id) {
		userShippingRepository.deleteById(id);
	}

}

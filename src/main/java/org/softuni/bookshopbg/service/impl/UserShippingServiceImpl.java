package org.softuni.bookshopbg.service.impl;


import org.softuni.bookshopbg.model.entities.UserShipping;
import org.softuni.bookshopbg.repositories.UserShippingRepository;
import org.softuni.bookshopbg.service.UserShippingService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserShippingServiceImpl implements UserShippingService {
	
	private UserShippingRepository userShippingRepository;

	public UserShippingServiceImpl(UserShippingRepository userShippingRepository) {
		this.userShippingRepository = userShippingRepository;
	}
	
	
	public Optional<UserShipping> findById(Long id) {
		return userShippingRepository.findById(id);
	}
	
	public void deleteById(Long id) {
		Optional<UserShipping> userShippingToDelete = findById(id);
		userShippingToDelete.get().getUser().getUserShippingList().remove(userShippingToDelete.get());
		userShippingToDelete.ifPresent(userShipping -> userShipping.setUser(null));
		userShippingRepository.deleteById(userShippingToDelete.get().getId());
	}

}

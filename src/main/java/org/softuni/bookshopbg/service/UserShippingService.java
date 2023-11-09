package org.softuni.bookshopbg.service;

import org.softuni.bookshopbg.model.entities.UserShipping;

import java.util.Optional;

public interface UserShippingService {
	Optional<UserShipping> findById(Long id);
	
	void deleteById(Long id);
}

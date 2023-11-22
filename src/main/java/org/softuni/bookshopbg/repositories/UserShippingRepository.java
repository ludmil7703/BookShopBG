package org.softuni.bookshopbg.repositories;


import org.softuni.bookshopbg.model.entities.UserShipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserShippingRepository extends JpaRepository<UserShipping, Long> {
	
	Optional<UserShipping> findById(Long id);

	UserShipping save(UserShipping userShipping);

	UserShipping findUserShippingByUserShippingName(String userShippingName);

	void deleteById(Long id);

}

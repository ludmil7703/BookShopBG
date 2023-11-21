package org.softuni.bookshopbg.repositories;


import org.softuni.bookshopbg.model.entities.UserPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPaymentRepository extends JpaRepository<UserPayment, Long> {

    Optional<UserPayment> findById(Long id);


    void deleteById(Long id);
}

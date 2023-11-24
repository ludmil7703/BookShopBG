package org.softuni.bookshopbg.repositories;

import org.softuni.bookshopbg.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);


    Optional<UserEntity> findByEmail(String email);




}

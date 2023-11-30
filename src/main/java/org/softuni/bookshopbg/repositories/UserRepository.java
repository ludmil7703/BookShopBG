package org.softuni.bookshopbg.repositories;

import org.softuni.bookshopbg.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserEntitiesByUsername(String username);

    Optional<UserEntity> findUserEntitiesByEmail(String email);

}

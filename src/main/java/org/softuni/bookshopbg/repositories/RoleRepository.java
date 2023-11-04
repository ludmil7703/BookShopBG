package org.softuni.bookshopbg.repositories;

import org.softuni.bookshopbg.model.entities.UserRoleEntity;
import org.softuni.bookshopbg.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
@Repository
public interface RoleRepository extends JpaRepository<UserRoleEntity, Long> {
    UserRoleEntity findByRole(UserRoleEnum role);
}

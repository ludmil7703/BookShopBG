package org.softuni.bookshopbg.init;

import org.softuni.bookshopbg.model.security.UserRoleEntity;
import org.softuni.bookshopbg.model.enums.UserRoleEnum;
import org.softuni.bookshopbg.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Component
public class RolesInit implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public RolesInit(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        boolean hasRoles = this.roleRepository.count() > 0;

        if(!hasRoles){
            List<UserRoleEntity> userRoleEntities = new ArrayList<>();
            Arrays.stream(UserRoleEnum.values())
                    .forEach(userRoleEnum -> {
                        UserRoleEntity userRoleEntity = new UserRoleEntity();
                        userRoleEntity.setRole(userRoleEnum);
                        userRoleEntities.add(userRoleEntity);
                    });
            this.roleRepository.saveAll(userRoleEntities);
        }

    }
}

package com.multiservice.AppService.repositories;

import com.multiservice.AppService.entities.Role;
import com.multiservice.AppService.entities.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}

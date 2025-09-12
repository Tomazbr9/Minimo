package com.tomazbr9.minimo.repository;

import com.tomazbr9.minimo.enums.RoleName;
import com.tomazbr9.minimo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(RoleName name);
}

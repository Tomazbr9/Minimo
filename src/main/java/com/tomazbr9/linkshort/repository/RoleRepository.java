package com.tomazbr9.linkshort.repository;

import com.tomazbr9.linkshort.enums.RoleName;
import com.tomazbr9.linkshort.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(RoleName name);
}

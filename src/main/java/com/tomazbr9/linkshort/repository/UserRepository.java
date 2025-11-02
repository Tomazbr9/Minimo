package com.tomazbr9.linkshort.repository;

import com.tomazbr9.linkshort.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String user);

    boolean existsByUsername(String username);
}

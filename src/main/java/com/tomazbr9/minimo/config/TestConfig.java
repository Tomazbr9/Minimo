package com.tomazbr9.minimo.config;

import com.tomazbr9.minimo.enums.RoleName;
import com.tomazbr9.minimo.model.Role;
import com.tomazbr9.minimo.repository.RoleRepository;
import com.tomazbr9.minimo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {

        Role adminRole = Role.builder()
                .name(RoleName.ROLE_ADMINISTRATOR)
                .build();

        Role customerRole = Role.builder()
                .name(RoleName.ROLE_USER)
                .build();

        roleRepository.saveAll(Arrays.asList(adminRole, customerRole));
    }
}
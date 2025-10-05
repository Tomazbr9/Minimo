package com.tomazbr9.minimo.config;

import com.tomazbr9.minimo.enums.RoleName;
import com.tomazbr9.minimo.model.Role;
import com.tomazbr9.minimo.model.Url;
import com.tomazbr9.minimo.model.User;
import com.tomazbr9.minimo.repository.RoleRepository;
import com.tomazbr9.minimo.repository.UrlRepository;
import com.tomazbr9.minimo.repository.UserRepository;
import com.tomazbr9.minimo.security.SecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {

        Role adminRole = Role.builder()
                .name(RoleName.ROLE_ADMINISTRATOR)
                .build();

        Role userRole = Role.builder()
                .name(RoleName.ROLE_USER)
                .build();

        User user = User.builder()
                .username("alex")
                .password(securityConfiguration.passwordEncoder().encode("12345"))
                .roles(List.of(userRole)).build();

        Url url1 = Url.builder()
                .urlName("link de afiliado")
                .originalUrl("http://hotmart.com/48028208208408420482048")
                .shortenedUrl("5fhkt89")
                .user(user)
                .totalClicks(10).build();

        Url url2 = Url.builder()
                .urlName("link de video do youtube")
                .originalUrl("http://youtube.com.br/83830830850385jfdj32")
                .shortenedUrl("23vofjv4")
                .user(user)
                .totalClicks(10).build();

        roleRepository.saveAll(Arrays.asList(adminRole, userRole));
        userRepository.save(user);
        urlRepository.saveAll(Arrays.asList(url1, url2));


    }
}
package com.tomazbr9.minimo.security;

import com.tomazbr9.minimo.security.filter.UserAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Ativa as configurações de segurança do Spring Security
public class SecurityConfiguration {

    @Autowired
    private UserAuthenticationFilter userAuthenticationFilter;

    // Endpoints que não exigem autenticação
    public static final String[] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/v1/auth/register",
            "/v1/auth/login",

            "/swagger-ui/**",
            "/v3/api-docs/**",

            "/*"
    };

    // Endpoints que exigem perfil de Usuário
    public static final String[] ENDPOINTS_USER = {
            "/v1/url/**",
            "/v1/users/**"

    };

    // Endpoints que exigem perfil de ADMIN
    public static final String[] ENDPOINTS_ADMIN = {

    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Desativa CSRF para APIs REST (sem sessões)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // API sem uso de sessões
                )

                .authorizeHttpRequests(auth -> auth

                        // Libera acesso público para os endpoints sem autenticação
                        .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Restringe acesso aos endpoints USER
                        .requestMatchers(ENDPOINTS_USER).hasRole("USER")

                        // Restringe acesso aos endpoints ADMINISTRATOR
                        .requestMatchers(ENDPOINTS_ADMIN).hasRole("ADMINISTRATOR")

                        // Qualquer outra rota exige autenticação
                        .anyRequest().authenticated()
                )

                // Adiciona o filtro de autenticação JWT antes do filtro padrão do Spring
                .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Permite usar o AuthenticationManager padrão do Spring para autenticação
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Utiliza o algoritmo BCrypt para codificar senhas
        return new BCryptPasswordEncoder();
    }
}
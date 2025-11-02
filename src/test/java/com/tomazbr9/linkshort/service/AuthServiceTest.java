package com.tomazbr9.linkshort.service;

import com.tomazbr9.linkshort.dto.authDTO.JwtTokenDTO;
import com.tomazbr9.linkshort.dto.authDTO.LoginDTO;
import com.tomazbr9.linkshort.dto.userDTO.UserRequestDTO;
import com.tomazbr9.linkshort.enums.RoleName;
import com.tomazbr9.linkshort.model.Role;
import com.tomazbr9.linkshort.model.User;
import com.tomazbr9.linkshort.repository.RoleRepository;
import com.tomazbr9.linkshort.repository.UserRepository;
import com.tomazbr9.linkshort.security.SecurityConfiguration;
import com.tomazbr9.linkshort.security.jwt.JwtTokenService;
import com.tomazbr9.linkshort.security.model.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private SecurityConfiguration securityConfiguration;

    @InjectMocks
    private AuthService authService;

    private UserRequestDTO userRequest;
    private LoginDTO loginRequest;
    private Role role;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        role = Role.builder()
                .id(UUID.randomUUID())
                .name(RoleName.ROLE_USER)
                .build();

        userRequest = new UserRequestDTO("bruno", "123", RoleName.ROLE_USER);
        loginRequest = new LoginDTO("bruno", "123");

        user = User.builder()
                .id(UUID.randomUUID())
                .username("bruno")
                .password("encodedPass")
                .roles(List.of(role))
                .build();
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(role));
        when(securityConfiguration.passwordEncoder()).thenReturn(org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());

        authService.registerUser(userRequest);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenRoleNotFound() {
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.registerUser(userRequest));

        assertEquals("Papel não encontrado", ex.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldAuthenticateUserAndReturnJwtToken() {
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtTokenService.generateToken(userDetails)).thenReturn("jwt-token");

        JwtTokenDTO tokenDTO = authService.authenticateUser(loginRequest);

        assertNotNull(tokenDTO);
        assertEquals("jwt-token", tokenDTO.token());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenService).generateToken(userDetails);
    }
}

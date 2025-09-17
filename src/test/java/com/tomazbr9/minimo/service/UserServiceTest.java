package com.tomazbr9.minimo.service;

import com.tomazbr9.minimo.dto.userDTO.UserResponseDTO;
import com.tomazbr9.minimo.dto.userDTO.UserUpdateDTO;
import com.tomazbr9.minimo.model.User;
import com.tomazbr9.minimo.repository.UserRepository;
import com.tomazbr9.minimo.security.model.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(UUID.randomUUID())
                .username("bruno")
                .password("123")
                .build();

        userDetails = new UserDetailsImpl(user);
    }

    @Test
    void shouldFindAuthenticatedUserSuccessfully() {
        when(userRepository.findByUsername("bruno")).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.findAuthenticatedUser(userDetails);

        assertNotNull(response);
        assertEquals("bruno", response.username());
        verify(userRepository).findByUsername("bruno");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByUsername("bruno")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.findAuthenticatedUser(userDetails));

        assertEquals("Usuário não encontrado.", ex.getMessage());
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        when(userRepository.findByUsername("bruno")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserUpdateDTO updateDTO = new UserUpdateDTO("novoBruno", "novaSenha");
        UserResponseDTO response = userService.updateUser(updateDTO, userDetails);

        assertNotNull(response);
        assertEquals("novoBruno", response.username());
        verify(userRepository).save(user);
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        when(userRepository.findByUsername("bruno")).thenReturn(Optional.of(user));

        userService.deleteUser(userDetails);

        verify(userRepository).delete(user);
    }
}


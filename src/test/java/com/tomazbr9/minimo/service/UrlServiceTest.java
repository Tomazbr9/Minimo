package com.tomazbr9.minimo.service;

import com.tomazbr9.minimo.dto.urlDTO.UrlRequestDTO;
import com.tomazbr9.minimo.dto.urlDTO.UrlResponseDTO;
import com.tomazbr9.minimo.exception.UrlAlreadyExistsException;
import com.tomazbr9.minimo.model.Url;
import com.tomazbr9.minimo.model.User;
import com.tomazbr9.minimo.repository.UrlRepository;
import com.tomazbr9.minimo.repository.UserRepository;
import com.tomazbr9.minimo.security.model.UserDetailsImpl;
import com.tomazbr9.minimo.util.ShortUrlGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UrlService urlService;

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
    void shouldCreateShortUrlSuccessfully() {
        UrlRequestDTO request = new UrlRequestDTO("myAlias", "http://example.com");

        when(userRepository.findByUsername("bruno")).thenReturn(Optional.of(user));
        when(urlRepository.existsByShortenedUrl("myAlias")).thenReturn(false);

        Url savedUrl = Url.builder()
                .id(UUID.randomUUID())
                .originalUrl("http://example.com")
                .shortenedUrl("myAlias")
                .user(user)
                .build();

        when(urlRepository.save(any(Url.class))).thenReturn(savedUrl);

        UrlResponseDTO response = urlService.createShortUrl(request, userDetails);

        assertNotNull(response);
        assertEquals("myAlias", response.shortenedUrl());
        assertEquals("http://example.com", response.originalUrl());
        verify(urlRepository).save(any(Url.class));
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        UrlRequestDTO request = new UrlRequestDTO("http://example.com", "alias");

        when(userRepository.findByUsername("bruno")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                urlService.createShortUrl(request, userDetails));

        assertEquals("Usuário não encontrado.", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionIfUrlAlreadyExists() {
        UrlRequestDTO request = new UrlRequestDTO("http://example.com", "alias");

        when(userRepository.findByUsername("bruno")).thenReturn(Optional.of(user));
        when(urlRepository.existsByShortenedUrl(anyString())).thenReturn(true); // <- corrigido

        UrlAlreadyExistsException ex = assertThrows(UrlAlreadyExistsException.class, () ->
                urlService.createShortUrl(request, userDetails));

        assertEquals("Url já existe!", ex.getMessage());
    }

    @Test
    void shouldGenerateShortUrlAutomaticallyWhenAliasIsNull() {
        UrlRequestDTO request = new UrlRequestDTO(null, "http://example.com");

        when(userRepository.findByUsername("bruno")).thenReturn(Optional.of(user));
        when(urlRepository.existsByShortenedUrl(anyString())).thenReturn(false);

        Url savedUrl = Url.builder()
                .id(UUID.randomUUID())
                .originalUrl("http://example.com")
                .shortenedUrl("abc123") // Mocking ShortUrlGenerator
                .user(user)
                .build();

        try (MockedStatic<ShortUrlGenerator> mockedStatic = mockStatic(ShortUrlGenerator.class)) {
            mockedStatic.when(ShortUrlGenerator::generateShortUrl).thenReturn("abc123");
            when(urlRepository.save(any(Url.class))).thenReturn(savedUrl);

            UrlResponseDTO response = urlService.createShortUrl(request, userDetails);

            assertNotNull(response);
            assertEquals("abc123", response.shortenedUrl());
            assertEquals("http://example.com", response.originalUrl());
        }
    }
}

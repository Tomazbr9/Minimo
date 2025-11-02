package com.tomazbr9.linkshort.service;

import com.tomazbr9.linkshort.dto.urlDTO.UrlResponseDTO;
import com.tomazbr9.linkshort.model.Url;
import com.tomazbr9.linkshort.model.User;
import com.tomazbr9.linkshort.repository.UrlRepository;
import com.tomazbr9.linkshort.repository.UserRepository;
import com.tomazbr9.linkshort.security.model.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
                .password("123456")
                .build();

        userDetails = new UserDetailsImpl(user);
    }

    // -------------------- findUrls --------------------
    @Test
    void shouldReturnUrlsForUser() {
        Url url = Url.builder()
                .id(UUID.randomUUID())
                .originalUrl("http://example.com")
                .shortenedUrl("alias")
                .user(user)
                .build();

        when(userRepository.findByUsername("bruno")).thenReturn(Optional.of(user));
        when(urlRepository.findUrlByUser(user)).thenReturn(List.of(url));

        List<UrlResponseDTO> urls = urlService.findUrls(userDetails);

        assertEquals(1, urls.size());
        assertEquals("alias", urls.get(0).shortenedUrl());
        assertEquals("http://example.com", urls.get(0).originalUrl());
    }

    // -------------------- findUrlById --------------------
    @Test
    void shouldReturnUrlById() {
        UUID id = UUID.randomUUID();
        Url url = Url.builder()
                .id(id)
                .originalUrl("http://example.com")
                .shortenedUrl("alias")
                .user(user)
                .build();

        when(urlRepository.findById(id)).thenReturn(Optional.of(url));

        UrlResponseDTO response = urlService.findUrlById(id, userDetails);

        assertNotNull(response);
        assertEquals("alias", response.shortenedUrl());
        assertEquals("http://example.com", response.originalUrl());
    }

    @Test
    void shouldThrowExceptionWhenUrlNotFoundById() {
        UUID id = UUID.randomUUID();

        when(urlRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                urlService.findUrlById(id, userDetails));

        assertEquals("Url não encontrada!", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUrlDoesNotBelongToUser() {
        UUID id = UUID.randomUUID();
        User anotherUser = User.builder()
                .id(UUID.randomUUID())
                .username("isael")
                .password("123456")
                .build();

        Url url = Url.builder()
                .id(id)
                .originalUrl("http://example.com")
                .shortenedUrl("alias")
                .user(anotherUser)
                .build();

        when(urlRepository.findById(id)).thenReturn(Optional.of(url));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                urlService.findUrlById(id, userDetails));

        assertEquals("Sem permissão ao recurso", ex.getMessage());
    }

    // -------------------- deleteUrl --------------------
    @Test
    void shouldDeleteUrlSuccessfully() {
        UUID id = UUID.randomUUID();
        Url url = Url.builder()
                .id(id)
                .originalUrl("http://example.com")
                .shortenedUrl("alias")
                .user(user)
                .build();

        when(urlRepository.findById(id)).thenReturn(Optional.of(url));

        urlService.deleteUrl(id, userDetails);

        verify(urlRepository).delete(url);
    }

    @Test
    void shouldThrowExceptionWhenDeletingUrlThatDoesNotBelongToUser() {
        UUID id = UUID.randomUUID();
        User anotherUser = User.builder()
                .id(UUID.randomUUID())
                .username("eduardo")
                .password("123456")
                .build();

        Url url = Url.builder()
                .id(id)
                .originalUrl("http://example.com")
                .shortenedUrl("alias")
                .user(anotherUser)
                .build();

        when(urlRepository.findById(id)).thenReturn(Optional.of(url));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                urlService.deleteUrl(id, userDetails));

        assertEquals("Sem permissão ao recurso", ex.getMessage());
    }
}
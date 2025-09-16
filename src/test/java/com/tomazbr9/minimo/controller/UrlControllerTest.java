package com.tomazbr9.minimo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomazbr9.minimo.dto.urlDTO.UrlRequestDTO;
import com.tomazbr9.minimo.dto.urlDTO.UrlResponseDTO;
import com.tomazbr9.minimo.security.filter.UserAuthenticationFilter;
import com.tomazbr9.minimo.security.jwt.JwtTokenService;
import com.tomazbr9.minimo.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UrlController.class)
@AutoConfigureMockMvc(addFilters = false)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UrlService urlService; // mock do service

    @MockBean
    JwtTokenService jwtTokenService;

    @MockBean
    UserAuthenticationFilter filter;

    private UrlRequestDTO requestDTO;
    private UrlResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new UrlRequestDTO("myAlias", "http://example.com");
        responseDTO = new UrlResponseDTO(UUID.randomUUID(), "myAlias", "http://example.com");
    }

    @Test
    void shouldCreateShortUrlSuccessfully() throws Exception {

        Mockito.when(urlService.createShortUrl(any(UrlRequestDTO.class), any()))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/v1/url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortenedUrl").value("myAlias"))
                .andExpect(jsonPath("$.originalUrl").value("http://example.com"));
    }
}

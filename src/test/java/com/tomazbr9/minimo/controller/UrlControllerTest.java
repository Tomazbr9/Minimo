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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
        requestDTO = new UrlRequestDTO("myUrl","myAlias", "http://example.com");
        responseDTO = new UrlResponseDTO(UUID.randomUUID(), "myAlias", 10, "myAlias", "http://example.com");
    }

    @Test
    void shouldReturnAllUserUrls() throws Exception {

        List<UrlResponseDTO> response = List.of(
                new UrlResponseDTO(UUID.randomUUID(), "myUrl1", 10, "myAlias1", "http://example.com"),
                new UrlResponseDTO(UUID.randomUUID(), "myUrl2", 10, "myAlias2", "http://example.com")
        );

        Mockito.when(urlService.findUrls(any())).thenReturn(response);

        mockMvc.perform(get("/v1/url")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].shortenedUrl").value("myAlias1"))
                .andExpect(jsonPath("$[0].originalUrl").value("http://example.com"))
                .andExpect(jsonPath("$[1].shortenedUrl").value("myAlias2"))
                .andExpect(jsonPath("$[1].originalUrl").value("http://example.com"));
    }

    @Test
    void shouldReturnUserUrlById() throws Exception {
        UUID id = UUID.randomUUID();

        UrlResponseDTO response = new UrlResponseDTO(
                id,
                "myUrl",
                10,
                "myAlias",
                "http://example.com"
        );

        Mockito.when(urlService.findUrlById(Mockito.eq(id), any()))
                .thenReturn(response);

        mockMvc.perform(get("/v1/url/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.shortenedUrl").value("myAlias"))
                .andExpect(jsonPath("$.originalUrl").value("http://example.com"));
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

    @Test
    void shouldDeleteUrlById() throws Exception {
        UUID id = UUID.randomUUID();

        // não precisa retornar nada, apenas garantir que o método será chamado
        Mockito.doNothing().when(urlService).deleteUrl(Mockito.eq(id), any());

        mockMvc.perform(delete("/v1/url/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // garante que o service foi realmente chamado
        Mockito.verify(urlService, Mockito.times(1))
                .deleteUrl(Mockito.eq(id), any());
    }
}

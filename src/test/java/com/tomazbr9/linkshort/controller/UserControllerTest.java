package com.tomazbr9.linkshort.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomazbr9.linkshort.dto.userDTO.UserResponseDTO;
import com.tomazbr9.linkshort.dto.userDTO.UserUpdateDTO;
import com.tomazbr9.linkshort.security.filter.UserAuthenticationFilter;
import com.tomazbr9.linkshort.security.jwt.JwtTokenService;
import com.tomazbr9.linkshort.security.model.UserDetailsImpl;
import com.tomazbr9.linkshort.service.UserService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @MockBean
    private UserAuthenticationFilter filter;

    private UserResponseDTO userResponse;
    private UserUpdateDTO updateDTO;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        userResponse = new UserResponseDTO(userId, "bruno");
        updateDTO = new UserUpdateDTO("novoBruno", "novaSenha");

        userDetails = Mockito.mock(UserDetailsImpl.class);
        Mockito.when(userDetails.getUsername()).thenReturn("bruno");
    }

    @Test
    void shouldReturnAuthenticatedUser() throws Exception {
        Mockito.when(userService.findAuthenticatedUser(any()))
                .thenReturn(userResponse);

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("bruno"));
    }


    @Test
    void shouldUpdateUserSuccessfully() throws Exception {
        UserResponseDTO updatedResponse = new UserResponseDTO(userResponse.id(), updateDTO.username());

        Mockito.when(userService.updateUser(any(UserUpdateDTO.class), any()))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedResponse.id().toString()))
                .andExpect(jsonPath("$.username").value("novoBruno"));
    }


    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(any(UserDetailsImpl.class));

        mockMvc.perform(delete("/v1/users"))
                .andExpect(status().isNoContent());
    }

}

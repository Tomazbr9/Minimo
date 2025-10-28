package com.tomazbr9.minimo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomazbr9.minimo.dto.authDTO.JwtTokenDTO;
import com.tomazbr9.minimo.dto.authDTO.LoginDTO;
import com.tomazbr9.minimo.dto.userDTO.UserRequestDTO;
import com.tomazbr9.minimo.enums.RoleName;
import com.tomazbr9.minimo.security.filter.UserAuthenticationFilter;
import com.tomazbr9.minimo.security.jwt.JwtTokenService;
import com.tomazbr9.minimo.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @MockBean
    private UserAuthenticationFilter filter;

    private UserRequestDTO userRequestDTO;
    private LoginDTO loginDTO;
    private JwtTokenDTO jwtTokenDTO;

    @BeforeEach
    void setUp() {
        userRequestDTO = new UserRequestDTO("bruno", "123456", RoleName.ROLE_USER);
        loginDTO = new LoginDTO("bruno", "123456");
        jwtTokenDTO = new JwtTokenDTO("fake-jwt-token");
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuário Criado com sucesso!"));

        Mockito.verify(authService).registerUser(any(UserRequestDTO.class));
    }

    @Test
    void shouldAuthenticateUserAndReturnToken() throws Exception {
        Mockito.when(authService.authenticateUser(any(LoginDTO.class))).thenReturn(jwtTokenDTO);

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtTokenDTO)));

        Mockito.verify(authService).authenticateUser(any(LoginDTO.class));
    }
}

package com.tomazbr9.linkshort.controller;

import com.tomazbr9.linkshort.dto.authDTO.JwtTokenDTO;
import com.tomazbr9.linkshort.dto.authDTO.LoginDTO;
import com.tomazbr9.linkshort.dto.userDTO.UserRequestDTO;
import com.tomazbr9.linkshort.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Autenticação",
        description = "Operações para registro e login de usuários"
)
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService service;

    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria um novo usuário no sistema com os dados fornecidos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para registrar o usuário",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserRequestDTO.class))
            )
            @RequestBody @Valid UserRequestDTO request) {

        logger.info("Recebida requisição para criar usuário: {}", request.username());

        service.registerUser(request);

        logger.info("Usuário criado com sucesso.");

        return new ResponseEntity<>("Usuário criado com sucesso!", HttpStatus.CREATED);
    }

    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza login com e-mail e senha, retornando um token JWT válido."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = JwtTokenDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/login")
    public ResponseEntity<JwtTokenDTO> authenticateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais de acesso (e-mail e senha)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginDTO.class))
            )
            @RequestBody LoginDTO request) {

        logger.info("Recebida requisição para logar usuário: {}", request.username());

        JwtTokenDTO token = service.authenticateUser(request);

        logger.info("Usuário logado com sucesso");

        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}

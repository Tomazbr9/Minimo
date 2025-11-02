package com.tomazbr9.linkshort.controller;

import com.tomazbr9.linkshort.dto.userDTO.UserResponseDTO;
import com.tomazbr9.linkshort.dto.userDTO.UserUpdateDTO;
import com.tomazbr9.linkshort.security.model.UserDetailsImpl;
import com.tomazbr9.linkshort.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Usuários",
        description = "Operações para recuperar, atualizar e deletar usuários"
)
@RestController
@RequestMapping("v1/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService service;

    @Operation(
            summary = "Buscar usuário autenticado",
            description = "Retorna os dados do usuário atualmente autenticado no sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    public ResponseEntity<UserResponseDTO> findAuthenticatedUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        logger.info("Recebida requisiçao para obter informações do usuário: {}", userDetails.getUsername());

        UserResponseDTO response = service.findAuthenticatedUser(userDetails);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza as informações do usuário autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping
    public ResponseEntity<UserResponseDTO> updateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para atualização do usuário",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserUpdateDTO.class))
            )
            @RequestBody UserUpdateDTO dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        logger.info("Requisição recebida para atualização dos dados do usuário: {}", userDetails.getUsername());

        UserResponseDTO response = service.updateUser(dto, userDetails);

        logger.info("Dados do usuário {} atualizados com sucesso.", userDetails.getUsername());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Excluir usuário",
            description = "Remove a conta do usuário autenticado do sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        logger.info("Requisição recebida para deletar usuaŕio: {}", userDetails.getUsername());

        service.deleteUser(userDetails);

        logger.info("Usuário {} deletado com sucesso", userDetails.getUsername());

        return ResponseEntity.noContent().build();
    }
}


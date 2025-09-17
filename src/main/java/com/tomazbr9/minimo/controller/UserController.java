package com.tomazbr9.minimo.controller;

import com.tomazbr9.minimo.dto.userDTO.UserResponseDTO;
import com.tomazbr9.minimo.dto.userDTO.UserUpdateDTO;
import com.tomazbr9.minimo.security.model.UserDetailsImpl;
import com.tomazbr9.minimo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

        UserResponseDTO response = service.updateUser(dto, userDetails);
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
        service.deleteUser(userDetails);
        return ResponseEntity.noContent().build();
    }
}


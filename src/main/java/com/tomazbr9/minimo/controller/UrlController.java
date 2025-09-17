package com.tomazbr9.minimo.controller;

import com.tomazbr9.minimo.dto.urlDTO.UrlRequestDTO;
import com.tomazbr9.minimo.dto.urlDTO.UrlResponseDTO;
import com.tomazbr9.minimo.security.model.UserDetailsImpl;
import com.tomazbr9.minimo.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Url",
        description = "Operações para recuperar, criar e deletar urls encurtadas"
)
@RestController
@RequestMapping("/v1/url")
public class UrlController {

    @Autowired
    private UrlService service;

    @Operation(
            summary = "Criar URL encurtada",
            description = "Recebe uma URL original e retorna uma versão encurtada associada ao usuário autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "URL encurtada criada com sucesso",
                    content = @Content(schema = @Schema(implementation = UrlResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    public ResponseEntity<UrlResponseDTO> createShortUrl(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da URL a ser encurtada",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UrlRequestDTO.class))
            )
            @RequestBody UrlRequestDTO request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UrlResponseDTO response = service.createShortUrl(request, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

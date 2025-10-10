package com.tomazbr9.minimo.controller;

import com.tomazbr9.minimo.dto.urlDTO.TotalClicksAndMostClickedDTO;
import com.tomazbr9.minimo.dto.urlDTO.UrlPatchDTO;
import com.tomazbr9.minimo.dto.urlDTO.UrlRequestDTO;
import com.tomazbr9.minimo.dto.urlDTO.UrlResponseDTO;
import com.tomazbr9.minimo.security.model.UserDetailsImpl;
import com.tomazbr9.minimo.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
            summary = "Listar URLs do usuário autenticado",
            description = "Retorna todas as URLs encurtadas associadas ao usuário logado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de URLs retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = UrlResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    public ResponseEntity<List<UrlResponseDTO>> findUrls(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<UrlResponseDTO> urlsList = service.findUrls(userDetails);
        return ResponseEntity.ok(urlsList);
    }

    @Operation(
            summary = "Buscar URL pelo ID",
            description = "Retorna a URL encurtada correspondente ao ID fornecido, apenas se pertencer ao usuário logado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = UrlResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "URL não encontrada ou não pertence ao usuário",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UrlResponseDTO> findUrlById(
            @Parameter(description = "ID da URL", required = true)
            @PathVariable UUID id,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UrlResponseDTO response = service.findUrlById(id, userDetails);
        return ResponseEntity.ok(response);
    }

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
            @RequestBody @Valid UrlRequestDTO request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UrlResponseDTO response = service.createShortUrl(request, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UrlPatchDTO> patchUrl(@PathVariable UUID id, @RequestBody UrlPatchDTO request, @AuthenticationPrincipal UserDetailsImpl userDetails){

        UrlPatchDTO response = service.patchUrl(id, request, userDetails);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUrl(@PathVariable UUID id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        service.deleteUrl(id, userDetails);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/totalclicksurls")
    public ResponseEntity<TotalClicksAndMostClickedDTO> totalClicksOfAllUrls(@AuthenticationPrincipal UserDetailsImpl userDetails){

        TotalClicksAndMostClickedDTO response = service.totalClicksOfAllUrls(userDetails);
        return ResponseEntity.ok(response);
    }
}

package com.tomazbr9.linkshort.controller;

import com.tomazbr9.linkshort.dto.urlDTO.TotalClicksAndMostClickedDTO;
import com.tomazbr9.linkshort.dto.urlDTO.UrlPutDTO;
import com.tomazbr9.linkshort.dto.urlDTO.UrlRequestDTO;
import com.tomazbr9.linkshort.dto.urlDTO.UrlResponseDTO;
import com.tomazbr9.linkshort.security.model.UserDetailsImpl;
import com.tomazbr9.linkshort.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Url",
        description = "Operações para recuperar, criar, atualizar e deletar URLs encurtadas."
)
@RestController
@RequestMapping("/api/url")
public class UrlController {

    private static final Logger logger = LoggerFactory.getLogger(UrlController.class);

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

        logger.info("Recebida requisição para exibir todas as urls do usuário.");

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

        logger.info("Recebida requisição para buscar url por id.");

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

        logger.info("Recebida requisição para criar url: {}", request.urlName());

        UrlResponseDTO response = service.createShortUrl(request, userDetails);

        logger.info("Url criada com sucesso.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Atualizar URL existente",
            description = "Permite atualizar informações de uma URL encurtada específica, caso ela pertença ao usuário autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "URL atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = UrlPutDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "URL não encontrada ou não pertence ao usuário",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<UrlPutDTO> putUrl(
            @Parameter(description = "ID da URL que será atualizada", required = true)
            @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novos dados da URL encurtada",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UrlPutDTO.class))
            )
            @RequestBody UrlPutDTO request,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        logger.info("Recebida requisição para atualizar dados da url do usuário");

        UrlPutDTO response = service.putUrl(id, request, userDetails);

        logger.info("Url atualizada com sucesso.");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Operation(
            summary = "Deletar URL encurtada",
            description = "Remove permanentemente uma URL encurtada específica, caso pertença ao usuário autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "URL deletada com sucesso",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "URL não encontrada ou não pertence ao usuário",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUrl(
            @Parameter(description = "ID da URL a ser deletada", required = true)
            @PathVariable UUID id,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        logger.info("Recebida requisição para deletar url do usuário");

        service.deleteUrl(id, userDetails);

        logger.info("Url deletada com sucesso.");

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obter estatísticas de cliques das URLs do usuário",
            description = "Retorna o total de cliques somados de todas as URLs do usuário autenticado e a URL mais clicada."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = TotalClicksAndMostClickedDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/totalclicksurls")
    public ResponseEntity<TotalClicksAndMostClickedDTO> totalClicksOfAllUrls(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        logger.info("Recebida requisição para retornar total de cliques e maior numero de cliques de uma url");

        TotalClicksAndMostClickedDTO response = service.totalClicksOfAllUrls(userDetails);
        return ResponseEntity.ok(response);
    }
}

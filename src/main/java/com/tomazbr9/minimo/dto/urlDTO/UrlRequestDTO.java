package com.tomazbr9.minimo.dto.urlDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UrlRequestDTO(

        String shortenedUrl,

        @NotBlank(message = "Url é obrigatória")
        String originalUrl
) {
}

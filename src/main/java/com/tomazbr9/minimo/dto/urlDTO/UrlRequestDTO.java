package com.tomazbr9.minimo.dto.urlDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public record UrlRequestDTO(

        @NotBlank(message = "Por favor, digite o nome da url")
        String urlName,

        String shortenedUrl,


        @URL(message = "URL inválida")
        @NotBlank(message = "Url é obrigatória")
        String originalUrl
) {
}

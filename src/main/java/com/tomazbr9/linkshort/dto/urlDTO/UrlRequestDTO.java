package com.tomazbr9.linkshort.dto.urlDTO;

import jakarta.validation.constraints.NotBlank;
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

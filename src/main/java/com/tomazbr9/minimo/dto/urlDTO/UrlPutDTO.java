package com.tomazbr9.minimo.dto.urlDTO;

import org.hibernate.validator.constraints.URL;

public record UrlPutDTO(

        String urlName,


        @URL(message = "URL inválida")
        String originalUrl
) {
}

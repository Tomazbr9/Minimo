package com.tomazbr9.minimo.dto.urlDTO;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record UrlPatchDTO(

        String urlName,

        String originalUrl
) {
}

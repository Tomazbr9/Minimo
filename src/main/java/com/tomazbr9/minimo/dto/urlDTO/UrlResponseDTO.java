package com.tomazbr9.minimo.dto.urlDTO;

import java.util.UUID;

public record UrlResponseDTO(

        UUID id,
        String urlName,
        Integer totalClicks,
        String shortenedUrl,
        String originalUrl
) {
}

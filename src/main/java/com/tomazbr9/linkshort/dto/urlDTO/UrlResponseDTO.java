package com.tomazbr9.linkshort.dto.urlDTO;

import java.time.LocalDate;
import java.util.UUID;

public record UrlResponseDTO(

        UUID id,
        String urlName,
        Integer totalClicks,
        String shortenedUrl,
        String originalUrl,
        LocalDate createdIn
) {
}

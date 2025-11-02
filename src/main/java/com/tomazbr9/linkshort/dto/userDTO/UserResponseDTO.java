package com.tomazbr9.linkshort.dto.userDTO;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username
) {
}

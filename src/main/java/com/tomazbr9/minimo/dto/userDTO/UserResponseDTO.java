package com.tomazbr9.minimo.dto.userDTO;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username
) {
}

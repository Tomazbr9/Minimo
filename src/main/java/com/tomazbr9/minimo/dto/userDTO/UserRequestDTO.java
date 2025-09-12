package com.tomazbr9.minimo.dto.userDTO;

import com.tomazbr9.minimo.enums.RoleName;

public record UserRequestDTO(
        String username,
        String password,
        RoleName role
) {
}

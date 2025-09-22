package com.tomazbr9.minimo.dto.userDTO;

import com.tomazbr9.minimo.enums.RoleName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(

        @NotBlank(message = "O username é obrigatório")
        @Size(min = 3, max = 50, message = "O username deve ter entre 3 e 50 caracteres")
        String username,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
        String password,

        @NotNull(message = "O role é obrigatório")
        RoleName role

) {}
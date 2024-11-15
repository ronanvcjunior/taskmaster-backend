package com.ronanvcjunior.taskmaster.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserLoginRequest(
        @NotEmpty(message = "O email não pode estar vazio ou ser nulo")
        @Email(message = "O email deve ser válido")
        String email,

        @NotEmpty(message = "A senha não pode estar vazia ou ser nula")
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        String password
) {
}

package com.ronanvcjunior.taskmaster.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserRequest(
        @NotEmpty(message = "O primeiro nome não pode estar vazio ou nulo")
        String firstName,

        @NotEmpty(message = "O último nome não pode estar vazio ou nulo")
        String lastName,

        @NotEmpty(message = "O email não pode estar vazio ou nulo")
        @Email(message = "O email deve ser válido")
        String email,

        @NotEmpty(message = "A senha não pode estar vazia ou nula")
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        String password
) {
}

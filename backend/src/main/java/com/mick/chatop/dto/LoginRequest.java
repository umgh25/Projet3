package com.mick.chatop.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Le champ login est obligatoire")
        String email,
        @NotBlank(message = "Le champ password est obligatoire")
        String password) {
}
// This record represents a request to log in, containing fields for email and password.
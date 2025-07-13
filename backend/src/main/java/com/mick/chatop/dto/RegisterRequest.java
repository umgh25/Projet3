package com.mick.chatop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "The email address is required.")
        @Email(message = "Please provide a valid email address.")
        String email,

        @NotBlank(message = "The name is required.")
        @Size(max = 255, message = "The name must not exceed 255 characters.")
        String name,

        @NotBlank(message = "The password is required.")
        @Size(min = 8, max = 100, message = "The password must be between 8 and 100 characters long.")
        String password
) {
}
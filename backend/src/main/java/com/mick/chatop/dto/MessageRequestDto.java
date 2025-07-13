package com.mick.chatop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MessageRequestDto(
        @NotNull(message = "The rental ID is required.")
        @JsonProperty("rental_id")
        Integer rentalId,

        @NotNull(message = "The user ID is required.")
        @JsonProperty("user_id")
        Integer userId,

        @NotBlank(message = "The message is required.")
        @Size(max = 2000, message = "The message must not exceed 2000 characters.")
        String message
) {
}
package com.mick.chatop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record UpdateRentalDto(
        Integer id,

        @NotBlank(message = "The name field is required.")
        @Size(max = 255, message = "The name must not exceed 255 characters.")
        String name,

        @NotNull(message = "The surface field is required.")
        Double surface,

        @NotNull(message = "The price field is required.")
        Double price,

        MultipartFile picture,

        @NotBlank(message = "The description field is required.")
        @Size(max = 2000, message = "The description must not exceed 2000 characters.")
        String description,

        @JsonProperty("owner_id")
        Integer ownerId
) {
}



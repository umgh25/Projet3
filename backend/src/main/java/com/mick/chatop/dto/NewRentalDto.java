package com.mick.chatop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record NewRentalDto(
        @NotBlank(message = "Le champ nom est obligatoire.")
        @Size(max = 255, message = "Le nom ne doit pas dépasser 255 caractères.")
        String name,

        @NotNull(message = "Le champ surface est obligatoire.")
        Double surface,

        @NotNull(message = "Le champ prix est obligatoire.")
        Double price,

        @NotNull(message = "La photo est obligatoire.")
        MultipartFile picture,

        @NotBlank(message = "Le champ description est obligatoire.")
        @Size(max = 2000, message = "La description ne doit pas dépasser 2000 caractères.")
        String description
) {
}

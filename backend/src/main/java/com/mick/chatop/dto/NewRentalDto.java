package com.mick.chatop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO utilisé pour créer une nouvelle annonce de location (rental).
 *
 * Ce record encapsule les données envoyées par le client lors de la création d'une annonce
 * incluant les champs obligatoires comme le nom, la surface, le prix, une image, et une description.
 *
 * Des contraintes de validation sont appliquées pour garantir l'intégrité et la cohérence des données.
 *
 * @param name        Nom de l’annonce (obligatoire, 255 caractères max)
 * @param surface     Surface en m² (obligatoire)
 * @param price       Prix de la location (obligatoire)
 * @param picture     Fichier image représentant le bien (obligatoire)
 * @param description Description détaillée de l’annonce (obligatoire, 2000 caractères max)
 */
public record NewRentalDto(
        @NotBlank(message = "The name field is required.")
        @Size(max = 255, message = "The name must not exceed 255 characters.")
        String name,

        @NotNull(message = "The surface field is required.")
        Double surface,

        @NotNull(message = "The price field is required.")
        Double price,

        @NotNull(message = "A photo is required.")
        MultipartFile picture,

        @NotBlank(message = "The description field is required.")
        @Size(max = 2000, message = "The description must not exceed 2000 characters.")
        String description
) {
}

package com.mick.chatop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO utilisé pour la mise à jour d'une annonce de location existante.
 *
 * Ce record contient les champs pouvant être modifiés par un utilisateur sur une annonce :
 * nom, surface, prix, description, image, etc.
 *
 * Des validations sont appliquées sur les champs principaux pour assurer la cohérence des données.
 * L'image est facultative lors de la mise à jour.
 *
 * @param id          Identifiant de l’annonce à modifier (obligatoire dans le contexte métier)
 * @param name        Nom de l’annonce (obligatoire, 255 caractères max)
 * @param surface     Surface de l’annonce en m² (obligatoire)
 * @param price       Prix de la location (obligatoire)
 * @param picture     Nouvelle image (facultative, de type multipart/form-data)
 * @param description Description de l’annonce (obligatoire, 2000 caractères max)
 * @param ownerId     Identifiant du propriétaire de l’annonce (exposé en JSON sous {@code owner_id})
 */
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




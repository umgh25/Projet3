package com.mick.chatop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la création d’un message.
 * 
 * Ce record est utilisé lors de l'envoi d’un message entre utilisateurs,
 * en lien avec une annonce (rental).
 * 
 * Il contient l’identifiant de l’annonce, l’identifiant de l’utilisateur,
 * ainsi que le contenu du message.
 *
 * Des contraintes de validation sont appliquées pour garantir l’intégrité des données.
 *
 * @param rentalId ID de l’annonce concernée (obligatoire)
 * @param userId ID de l’expéditeur du message (obligatoire)
 * @param message Contenu du message, limité à 2000 caractères (obligatoire)
 */
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

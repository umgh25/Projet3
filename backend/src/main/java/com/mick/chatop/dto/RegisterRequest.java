package com.mick.chatop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilisé pour l'inscription (enregistrement) d'un nouvel utilisateur.
 *
 * Ce record contient les informations nécessaires à la création d’un compte utilisateur :
 * email, nom et mot de passe.
 *
 * Des contraintes de validation sont appliquées pour garantir la validité des données saisies.
 *
 * @param email    Adresse email de l'utilisateur (obligatoire, format valide requis)
 * @param name     Nom de l'utilisateur (obligatoire, 255 caractères max)
 * @param password Mot de passe de l'utilisateur (obligatoire, entre 8 et 100 caractères)
 */
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

package com.mick.chatop.dto;

import java.time.LocalDateTime;

/**
 * DTO de consultation d’un utilisateur.
 *
 * Ce record est utilisé pour exposer les informations publiques d’un utilisateur,
 * généralement dans les réponses des API.
 *
 * @param id         Identifiant unique de l’utilisateur
 * @param email      Adresse email de l’utilisateur
 * @param name       Nom de l’utilisateur
 * @param created_at Date de création du compte utilisateur
 * @param updated_at Date de dernière mise à jour des informations utilisateur
 */
public record UserDto(
        Integer id,
        String email,
        String name,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}

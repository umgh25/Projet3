package com.mick.chatop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * DTO de consultation d'une annonce de location.
 *
 * Ce record représente les données exposées au client lorsqu’une annonce
 * de location (rental) est consultée (liste ou détail).
 *
 * @param id          Identifiant unique de l’annonce
 * @param name        Nom ou titre de l’annonce
 * @param surface     Surface en mètres carrés
 * @param price       Prix de la location
 * @param description Description de l’annonce
 * @param picture     URL ou chemin de l’image associée
 * @param ownerId     Identifiant du propriétaire (exposé sous forme de {@code owner_id} en JSON)
 * @param createdAt   Date de création de l’annonce
 * @param updatedAt   Date de dernière mise à jour de l’annonce
 */
public record RentalDto(
        Integer id,
        String name,
        Double surface,
        Double price,
        String description,
        String picture,
        @JsonProperty("owner_id")
        Integer ownerId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

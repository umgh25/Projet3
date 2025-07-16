package com.mick.chatop.service;

import com.mick.chatop.dto.NewRentalDto;
import com.mick.chatop.dto.RentalDto;
import com.mick.chatop.dto.UpdateRentalDto;

import java.util.List;

/**
 * Interface définissant les opérations disponibles pour gérer les biens à louer (rentals).
 */
public interface RentalService {

    /**
     * Récupère la liste complète des biens à louer.
     *
     * @return Une liste d'objets {@link RentalDto} représentant les locations disponibles.
     */
    List<RentalDto> getAllRentals();

    /**
     * Récupère les détails d'une location spécifique à partir de son identifiant.
     *
     * @param id L'identifiant unique de la location.
     * @return Un objet {@link RentalDto} correspondant à la location, ou {@code null} si non trouvée.
     */
    RentalDto getRentalById(Integer id);

    /**
     * Crée une nouvelle location à partir des données fournies.
     *
     * @param rentalDto Données de la nouvelle location à créer.
     */
    void createRental(NewRentalDto rentalDto);

    /**
     * Met à jour une location existante à partir de son identifiant et des nouvelles données.
     *
     * @param id                Identifiant de la location à mettre à jour.
     * @param updateRentalDto   Nouvelles données à appliquer à la location existante.
     */
    void updateRental(Integer id, UpdateRentalDto updateRentalDto);
}
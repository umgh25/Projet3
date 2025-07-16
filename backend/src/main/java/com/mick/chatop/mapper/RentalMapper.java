package com.mick.chatop.mapper;

import com.mick.chatop.dto.NewRentalDto;
import com.mick.chatop.dto.RentalDto;
import com.mick.chatop.entity.RentalEntity;
import org.springframework.stereotype.Component;


/**
 * Mapper responsable de la conversion entre les entités {@link RentalEntity}
 * et les objets de transfert de données (DTO) {@link RentalDto} et {@link NewRentalDto}.
 */
@Component
public class RentalMapper {

    /**
     * Convertit une entité {@link RentalEntity} en objet {@link RentalDto}.
     *
     * @param rentalEntity L'entité représentant une location en base de données.
     * @return Un DTO contenant les informations de la location.
     */
    public RentalDto toDTO(RentalEntity rentalEntity) {
        return new RentalDto(
                rentalEntity.getId(),
                rentalEntity.getName(),
                rentalEntity.getSurface(),
                rentalEntity.getPrice(),
                rentalEntity.getDescription(),
                rentalEntity.getPicture(),
                rentalEntity.getOwner().getId(),
                rentalEntity.getCreated_at(),
                rentalEntity.getUpdated_at()
        );
    }

    /**
     * Convertit un objet {@link NewRentalDto} en entité {@link RentalEntity}.
     * L'owner et l'image ne sont pas inclus ici et doivent être ajoutés séparément.
     *
     * @param dto Le DTO contenant les données de création d'une nouvelle location.
     * @return Une nouvelle instance de {@link RentalEntity} initialisée avec les données du DTO.
     */
    public RentalEntity toEntity(NewRentalDto dto) {
        return new RentalEntity(dto.name(), dto.surface(), dto.price(), dto.description());
    }
}

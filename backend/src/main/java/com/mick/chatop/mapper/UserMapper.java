package com.mick.chatop.mapper;

import com.mick.chatop.dto.RegisterRequest;
import com.mick.chatop.dto.UserDto;
import com.mick.chatop.entity.UserEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper responsable de la conversion entre {@link UserEntity} et les objets de transfert
 * {@link RegisterRequest} (pour l'inscription) et {@link UserDto} (pour l'affichage des utilisateurs).
 */
@Component
public class UserMapper {

    /**
     * Convertit une requête d'inscription {@link RegisterRequest} en entité {@link UserEntity}.
     *
     * @param registerRequest L'objet contenant les données d'inscription de l'utilisateur.
     * @param hashedPassword  Le mot de passe déjà haché à stocker dans l'entité.
     * @return Une nouvelle instance de {@link UserEntity} avec les données de l'utilisateur.
     */
    public UserEntity toEntity(RegisterRequest registerRequest, String hashedPassword) {
        return new UserEntity(
                registerRequest.email(),
                registerRequest.name(),
                hashedPassword
        );
    }

    /**
     * Convertit une entité {@link UserEntity} en objet de transfert {@link UserDto}.
     *
     * @param userEntity L'entité représentant l'utilisateur en base de données.
     * @return Un objet {@link UserDto} contenant les informations publiques de l'utilisateur.
     */
    public UserDto toDto(UserEntity userEntity) {
        return new UserDto(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getName(),
                userEntity.getCreated_at(),
                userEntity.getUpdated_at()
        );
    }
}
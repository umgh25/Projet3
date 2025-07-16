package com.mick.chatop.mapper;

import com.mick.chatop.dto.MessageRequestDto;
import com.mick.chatop.entity.MessageEntity;
import com.mick.chatop.entity.RentalEntity;
import com.mick.chatop.entity.UserEntity;
import org.springframework.stereotype.Component;


/**
 * Mapper permettant de convertir les objets liés aux messages
 * entre les couches DTO et entité.
 */
@Component
public class MessageMapper {

    /**
     * Convertit une requête de message (DTO) en entité {@link MessageEntity}.
     *
     * @param rental              L'entité de location associée au message.
     * @param user                L'utilisateur auteur du message.
     * @param messageRequestDto   Le DTO contenant le contenu du message.
     * @return Une nouvelle instance de {@link MessageEntity} construite à partir des données fournies.
     */
    public MessageEntity toEntity(RentalEntity rental, UserEntity user,
                                  MessageRequestDto messageRequestDto) {
        return new MessageEntity(rental, user, messageRequestDto.message());
    }
}

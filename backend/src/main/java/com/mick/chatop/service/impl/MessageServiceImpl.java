package com.mick.chatop.service.impl;

import com.mick.chatop.dto.MessageRequestDto;
import com.mick.chatop.entity.MessageEntity;
import com.mick.chatop.entity.RentalEntity;
import com.mick.chatop.entity.UserEntity;
import com.mick.chatop.mapper.MessageMapper;
import com.mick.chatop.repository.MessageRepository;
import com.mick.chatop.repository.RentalRepository;
import com.mick.chatop.repository.UserRepository;
import com.mick.chatop.service.MessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implémentation du service de gestion des messages.
 * Cette classe gère la création de messages dans le système.
 * Ce service permet de créer un nouveau message lié à une location
 * et un utilisateur dans le système.
 */
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

    /**
     * Constructeur avec injection des dépendances nécessaires au service.
     *
     * @param messageRepository   Repository pour l'entité {@link MessageEntity}.
     * @param messageMapper       Mapper permettant de transformer les DTO en entités.
     * @param userRepository      Repository pour l'entité {@link UserEntity}.
     * @param rentalRepository    Repository pour l'entité {@link RentalEntity}.
     */
    public MessageServiceImpl(
            MessageRepository messageRepository,
            MessageMapper messageMapper,
            UserRepository userRepository,
            RentalRepository rentalRepository
    ) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
    }

    /**
     * Crée un nouveau message à partir des informations fournies dans le DTO.
     * <p>
     * Le message est lié à un utilisateur et une location existants. S'il manque l'un des deux,
     * une exception est levée.
     *
     * @param messageRequestDto DTO contenant les informations du message (texte, userId, rentalId).
     * @throws RuntimeException si l'utilisateur ou la location n'existe pas.
     */
    @Override
    public void createMessage(MessageRequestDto messageRequestDto) {
        UserEntity user = userRepository.findById(messageRequestDto.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        RentalEntity rental = rentalRepository.findById(messageRequestDto.rentalId())
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        MessageEntity messageEntity = messageMapper.toEntity(rental, user, messageRequestDto);
        messageEntity.setCreated_at(LocalDateTime.now());
        messageEntity.setUpdated_at(LocalDateTime.now());

        messageRepository.save(messageEntity);
    }
}
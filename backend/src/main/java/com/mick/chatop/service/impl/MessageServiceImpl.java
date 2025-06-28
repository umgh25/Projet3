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

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

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

    @Override
    public void createMessage(MessageRequestDto messageRequestDto) {
        UserEntity user = userRepository.findById(messageRequestDto.userId())
                .orElseThrow(() -> new RuntimeException("Utilisateur inconnu"));

        RentalEntity rental = rentalRepository.findById(messageRequestDto.rentalId())
                .orElseThrow(() -> new RuntimeException("Location inconnue"));

        MessageEntity messageEntity = messageMapper.toEntity(rental, user, messageRequestDto);
        messageEntity.setCreated_at(LocalDateTime.now());
        messageEntity.setUpdated_at(LocalDateTime.now());
        messageRepository.save(messageEntity);
    }
}
package com.mick.chatop.service;

import com.mick.chatop.dto.MessageRequestDto;

// Cette interface définit les méthodes du service de gestion des messages
public interface MessageService {

    void createMessage(MessageRequestDto messageRequestDto);
}
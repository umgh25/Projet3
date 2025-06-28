package com.mick.chatop.service;

import com.mick.chatop.dto.MessageRequestDto;

public interface MessageService {

    void createMessage(MessageRequestDto messageRequestDto);
}
package com.mick.chatop.mapper;

import com.mick.chatop.dto.MessageRequestDto;
import com.mick.chatop.entity.MessageEntity;
import com.mick.chatop.entity.RentalEntity;
import com.mick.chatop.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageEntity toEntity(RentalEntity rental, UserEntity user,
                                  MessageRequestDto messageRequestDto) {
        return new MessageEntity(rental, user, messageRequestDto.message());
    }
}
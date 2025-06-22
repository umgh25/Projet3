package com.mick.chatop.mapper;

import com.mick.chatop.dto.RegisterRequest;
import com.mick.chatop.dto.UserDto;
import com.mick.chatop.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserEntity toEntity(RegisterRequest registerRequest, String hashedPassword) {
        return new UserEntity(registerRequest.email(), registerRequest.name(), hashedPassword);
    }

    public UserDto toDto(UserEntity userEntity) {
        return new UserDto(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getName(),
                userEntity.getCreated_at().toString(),
                userEntity.getUpdated_at().toString()
        );
    }
}

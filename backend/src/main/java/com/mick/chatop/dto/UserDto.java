package com.mick.chatop.dto;

import java.time.LocalDateTime;

public record UserDto(
        Integer id,
        String email,
        String name,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {

}

// This record represents a user data transfer object (DTO) in the application.
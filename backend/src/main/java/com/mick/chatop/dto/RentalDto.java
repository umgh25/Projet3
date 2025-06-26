package com.mick.chatop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record RentalDto(
        Integer id,
        String name,
        Double surface,
        Double price,
        String description,
        String picture,
        @JsonProperty("owner_id")
        Integer ownerId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

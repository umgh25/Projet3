package com.mick.chatop.dto;

public record UserDto(Integer id, String email, String name, String created_at, String updated_at) {
}
// This record represents a user in the system, with fields for ID, email, name, and timestamps for creation and last update.
package com.mick.chatop.dto;

public record RegisterRequest(String email, String name, String password) {
}
// This record represents a request to register a new user, containing fields for email, name, and password.

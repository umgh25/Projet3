package com.mick.chatop.service;

import com.mick.chatop.dto.AuthResponse;
import com.mick.chatop.dto.LoginRequest;
import com.mick.chatop.dto.RegisterRequest;
import com.mick.chatop.dto.UserDto;
import org.springframework.security.core.Authentication;

public interface UserService {

    AuthResponse login(LoginRequest loginRequest) throws Exception;
    AuthResponse register(RegisterRequest registerRequest);
    UserDto getAuthenticatedUser(Authentication authentication);
}

package com.mick.chatop.service.impl;

import com.mick.chatop.dto.AuthResponse;
import com.mick.chatop.dto.LoginRequest;
import com.mick.chatop.dto.RegisterRequest;
import com.mick.chatop.dto.UserDto;
import com.mick.chatop.entity.UserEntity;
import com.mick.chatop.mapper.UserMapper;
import com.mick.chatop.repository.UserRepository;
import com.mick.chatop.security.JwtService;
import com.mick.chatop.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(authentication);
        return new AuthResponse(token);
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        String hashedPassword = passwordEncoder.encode(registerRequest.password());
        UserEntity newUser = userMapper.toEntity(registerRequest, hashedPassword);
        newUser.setCreated_at(LocalDateTime.now());
        newUser.setUpdated_at(LocalDateTime.now());
        userRepository.save(newUser);
        System.out.println("nouveau : " + newUser);
        String token = jwtService.generateToken(
                new UsernamePasswordAuthenticationToken(registerRequest.email(),
                        null,
                        Collections.singleton(new SimpleGrantedAuthority("USER"))));
        return new AuthResponse(token);
    }

    @Override
    public UserDto getAuthenticatedUser(Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("L'utilisateur n'existe pas"));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto getUserById(Integer id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("L'utilisateur n'existe pas"));
        return userMapper.toDto(user);
    }
}

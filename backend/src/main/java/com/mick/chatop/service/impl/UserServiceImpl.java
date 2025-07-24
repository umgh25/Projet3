package com.mick.chatop.service.impl;

import com.mick.chatop.dto.AuthResponse;
import com.mick.chatop.dto.LoginRequest;
import com.mick.chatop.dto.RegisterRequest;
import com.mick.chatop.dto.UserDto;
import com.mick.chatop.entity.TokenEntity;
import com.mick.chatop.entity.UserEntity;
import com.mick.chatop.mapper.UserMapper;
import com.mick.chatop.repository.TokenRepository;
import com.mick.chatop.repository.UserRepository;
import com.mick.chatop.security.JwtService;
import com.mick.chatop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;

/**
 * Implémentation du service utilisateur {@link UserService}.
 * Fournit la logique métier pour l'authentification, l'inscription,
 * et la récupération des informations utilisateur.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final TokenRepository tokenRepository;

    public UserServiceImpl(UserRepository userRepository,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper,
                           TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.tokenRepository = tokenRepository;
    }
    /**
     * Authentifie l'utilisateur avec les informations fournies et génère un token JWT.
     * Enregistre le token dans la base de données pour une utilisation ultérieure.
     *
     * @param request les informations de connexion de l'utilisateur
     * @return un objet AuthResponse contenant le token JWT
     */
    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtService.generateToken(authentication);

        UserEntity user = userRepository.findByEmail(request.email()).orElseThrow();

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setUser(user);
        tokenEntity.setCreatedAt(Instant.now());
        tokenEntity.setValid(true);
        tokenRepository.save(tokenEntity);

        logger.debug("Token généré et enregistré en BDD pour l'utilisateur : {}", user.getEmail());

        return new AuthResponse(token);
    }
    // Méthode pour enregistrer un nouvel utilisateur
    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        String hashedPassword = passwordEncoder.encode(registerRequest.password());
        UserEntity newUser = userMapper.toEntity(registerRequest, hashedPassword);
        newUser.setCreated_at(LocalDateTime.now());
        newUser.setUpdated_at(LocalDateTime.now());
        userRepository.save(newUser);

        String token = jwtService.generateToken(
                new UsernamePasswordAuthenticationToken(registerRequest.email(),
                        null,
                        Collections.singleton(new SimpleGrantedAuthority("USER"))));

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setUser(newUser);
        tokenEntity.setCreatedAt(Instant.now());
        tokenEntity.setValid(true);
        tokenRepository.save(tokenEntity);

        logger.info("Nouvel utilisateur enregistré avec succès : {}", newUser.getEmail());

        return new AuthResponse(token);
    }
    // Méthode pour récupérer l'utilisateur authentifié
    @Override
    public UserDto getAuthenticatedUser(Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        return userMapper.toDto(user);
    }
    // Méthode pour récupérer un utilisateur par son ID
    @Override
    public UserDto getUserById(Integer id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        return userMapper.toDto(user);
    }
    // Méthode pour gérer la déconnexion de l'utilisateur
    @Override
    public void logout(String token) {
        tokenRepository.findByToken(token).ifPresentOrElse(tokenEntity -> {
            logger.debug("Token trouvé pour logout : {}", tokenEntity.getToken());
            tokenEntity.setValid(false);
            tokenRepository.save(tokenEntity);
            logger.debug("Token invalidé avec succès.");
        }, () -> {
            logger.warn("Token NON trouvé lors du logout !");
        });
    }
}

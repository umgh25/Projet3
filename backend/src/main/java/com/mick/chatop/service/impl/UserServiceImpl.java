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

/**
 * Implémentation du service utilisateur {@link UserService}.
 * 
 * Fournit la logique métier pour l'authentification, l'inscription,
 * et la récupération des informations utilisateur.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param userRepository         Le repository pour les entités {@link UserEntity}.
     * @param jwtService             Service de génération de token JWT.
     * @param authenticationManager  Gestionnaire d'authentification Spring Security.
     * @param passwordEncoder        Encodeur de mots de passe.
     * @param userMapper             Mapper entre entités et DTOs utilisateurs.
     */
    public UserServiceImpl(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    /**
     * Authentifie un utilisateur avec email et mot de passe.
     *
     * @param request Les informations de connexion (email, mot de passe).
     * @return Une réponse contenant un token JWT.
     * @throws AuthenticationException si l'authentification échoue.
     */
    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(authentication);
        return new AuthResponse(token);
    }

    /**
     * Enregistre un nouvel utilisateur après vérification de l'unicité de l'email.
     *
     * @param registerRequest Les données d'inscription (nom, email, mot de passe).
     * @return Une réponse contenant un token JWT.
     * @throws IllegalArgumentException si l'email est déjà utilisé.
     */
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
        return new AuthResponse(token);
    }

    /**
     * Récupère les informations de l'utilisateur actuellement authentifié.
     *
     * @param authentication L'objet d'authentification contenant l'email de l'utilisateur.
     * @return Un {@link UserDto} représentant l'utilisateur connecté.
     * @throws RuntimeException si l'utilisateur n'existe pas.
     */
    @Override
    public UserDto getAuthenticatedUser(Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        return userMapper.toDto(user);
    }

    /**
     * Récupère les informations d'un utilisateur par son identifiant.
     *
     * @param id L'identifiant de l'utilisateur.
     * @return Un {@link UserDto} correspondant à l'utilisateur trouvé.
     * @throws RuntimeException si l'utilisateur n'existe pas.
     */
    @Override
    public UserDto getUserById(Integer id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        return userMapper.toDto(user);
    }
}

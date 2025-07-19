package com.mick.chatop.service;

import com.mick.chatop.dto.AuthResponse;
import com.mick.chatop.dto.LoginRequest;
import com.mick.chatop.dto.RegisterRequest;
import com.mick.chatop.dto.UserDto;
import org.springframework.security.core.Authentication;

/**
 * Interface définissant les opérations liées à l'authentification et à la gestion des utilisateurs.
 */
public interface UserService {

    /**
     * Authentifie un utilisateur à partir de ses identifiants.
     *
     * @param loginRequest Données de connexion (email, mot de passe).
     * @return Une réponse contenant un token JWT si l'authentification est réussie.
     * @throws Exception Si les identifiants sont invalides ou une erreur se produit.
     */
    AuthResponse login(LoginRequest loginRequest) throws Exception;

    /**
     * Enregistre un nouvel utilisateur dans le système.
     *
     * @param registerRequest Données d'inscription (email, nom, mot de passe).
     * @return Une réponse contenant un token JWT après inscription réussie.
     */
    AuthResponse register(RegisterRequest registerRequest);

    /**
     * Récupère les informations de l'utilisateur actuellement authentifié.
     *
     * @param authentication Objet Spring Security représentant l'utilisateur connecté.
     * @return Un DTO {@link UserDto} contenant les données de l'utilisateur.
     */
    UserDto getAuthenticatedUser(Authentication authentication);

    /**
     * Récupère les informations d'un utilisateur à partir de son identifiant.
     *
     * @param id L'identifiant unique de l'utilisateur.
     * @return Un objet {@link UserDto} représentant l'utilisateur correspondant.
     */
    UserDto getUserById(Integer id);

    /**
     * Invalide le token JWT courant (déconnexion).
     *
     * @param token Le token JWT à invalider.
     */
    void logout(String token);
}
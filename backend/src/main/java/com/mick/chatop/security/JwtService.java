package com.mick.chatop.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Service responsable de la génération de tokens JWT pour les utilisateurs authentifiés.
 * 
 * Utilise {@link JwtEncoder} fourni par Spring Security pour signer le token.
 */
@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    /**
     * Constructeur injectant l'encodeur JWT.
     *
     * @param jwtEncoder Composant responsable de l'encodage (signature) des JWT.
     */
    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Génère un token JWT valide pendant 1 heure pour un utilisateur authentifié.
     *
     * @param authentication Objet Spring Security représentant l'utilisateur authentifié.
     * @return Une chaîne représentant le token JWT signé.
     */
    public String generateToken(Authentication authentication) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self") // Émetteur du token (self = l'application elle-même)
                .issuedAt(Instant.now()) // Date de création du token
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS)) // Expiration dans 1 heure
                .subject(authentication.getName()) // Identifiant unique de l'utilisateur
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS512).build(),
                claims
        );

        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
}
package com.mick.chatop.security;

import com.mick.chatop.repository.TokenRepository;
import com.mick.chatop.entity.TokenEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Optional;

/**
 * Filtre pour valider les tokens JWT stockés dans la base de données.
 * Vérifie si le token est présent et valide avant de permettre l'accès aux endpoints sécurisés.
 */
@Component
public class JwtTokenDatabaseFilter extends OncePerRequestFilter {
    private final TokenRepository tokenRepository;

    public JwtTokenDatabaseFilter(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
    /**
     * Filtre les requêtes HTTP pour vérifier la validité du token JWT.
     * Si le token est invalide ou absent, renvoie une réponse 401 Unauthorized.
     *
     * @param request  la requête HTTP
     * @param response la réponse HTTP
     * @param filterChain la chaîne de filtres
     * @throws ServletException si une erreur de servlet se produit
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        System.out.println("[DEBUG] JwtTokenDatabaseFilter exécuté pour URI : " + request.getRequestURI());
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("[DEBUG] Token extrait : " + token);
            Optional<TokenEntity> tokenEntityOpt = tokenRepository.findByToken(token);
            if (tokenEntityOpt.isEmpty() || !tokenEntityOpt.get().isValid()) {
                System.out.println("[DEBUG] Token non trouvé ou invalide en BDD !");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("{\"error\":\"UNAUTHORIZED\",\"status\":401,\"message\":\"Token invalid or revoked\"}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
} 
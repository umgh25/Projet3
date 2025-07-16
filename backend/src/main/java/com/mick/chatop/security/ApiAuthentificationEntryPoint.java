package com.mick.chatop.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mick.chatop.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * Point d'entrée personnalisé pour gérer les erreurs d'authentification dans Spring Security.
 * 
 * Cette classe est déclenchée automatiquement lorsqu'un utilisateur non authentifié
 * tente d'accéder à une ressource protégée.
 * Elle renvoie une réponse JSON structurée avec un code 401.
 */
public class ApiAuthentificationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Méthode appelée automatiquement lorsqu'un utilisateur non authentifié accède à une route protégée.
     *
     * @param request       La requête HTTP entrante.
     * @param response      La réponse HTTP à retourner.
     * @param authException L'exception d'authentification déclenchée par Spring Security.
     * @throws IOException      En cas d'erreur d'écriture dans la réponse.
     * @throws ServletException Jamais levée ici, mais exigée par la signature.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = new ErrorResponse(
                "UNAUTHORIZED",
                401,
                "Unauthenticated user"
        );

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}
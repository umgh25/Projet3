package com.mick.chatop.config;

import com.mick.chatop.security.ApiAuthentificationEntryPoint;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import com.mick.chatop.repository.TokenRepository;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import com.mick.chatop.security.JwtTokenDatabaseFilter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /**
     * Clé secrète pour signer et vérifier les tokens JWT.
     * Elle est injectée depuis le fichier de configuration (application.properties).
     */
    @Value("${jwt.secret}")
    private String secretKey;
    /**
     * Repository pour accéder aux tokens JWT stockés dans la base de données.
     * Utilisé pour valider les tokens lors des requêtes sécurisées.
     */
    @Autowired
    private TokenRepository tokenRepository;
    /**
     * Filtre personnalisé pour valider les tokens JWT stockés dans la base de données.
     * Il est ajouté avant le filtre BearerTokenAuthenticationFilter de Spring Security.
     */
    @Autowired
    private JwtTokenDatabaseFilter jwtTokenDatabaseFilter;

    /**
     * Bean pour encoder les mots de passe.
     * Utilise BCrypt, un algorithme de hachage robuste adapté aux mots de passe.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Point d'entrée personnalisé pour gérer les requêtes non authentifiées.
     * Retourne une réponse JSON normalisée en cas d'accès refusé.
     */
    @Bean
    public AuthenticationEntryPoint apiAuthenticationEntryPoint() {
        return new ApiAuthentificationEntryPoint();
    }

    /**
     * Chaîne de filtres de sécurité HTTP.
     * - Désactive CSRF car l’API est stateless.
     * - Déclare les endpoints publics accessibles sans authentification.
     * - Toutes les autres requêtes nécessitent un JWT valide.
     * - Configure le serveur de ressources OAuth2 pour la validation des JWT.
     * - Déclare le point d’entrée personnalisé pour gérer les erreurs d’authentification.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/api/auth/login",
                    "/api/auth/register",
                    "/api/rentals/image/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(apiAuthenticationEntryPoint())
            );
        // Ajouter le filtre AVANT BearerTokenAuthenticationFilter
        http.addFilterBefore(jwtTokenDatabaseFilter, BearerTokenAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Bean pour signer les tokens JWT.
     * Utilise Nimbus et une clé secrète immuable.
     */
    @Bean
    JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes()));
    }

    /**
     * Bean pour décoder et vérifier les tokens JWT.
     * Définit l’algorithme de signature HMAC SHA-512.
     */
    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA512");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    /**
     * Expose l'AuthenticationManager de Spring Security.
     * Utilisé pour authentifier les utilisateurs via le flux standard.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

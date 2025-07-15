package com.mick.chatop.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfiguration {

    /**
     * URL de l'API pour l'environnement de développement.
     * Injectée depuis le fichier de configuration (application.properties).
     */
    @Value("${chatop.openapi.dev-url}")
    private String devUrl;

    /**
     * URL de l'API pour l'environnement de production.
     * Injectée depuis le fichier de configuration (application.properties).
     */
    @Value("${chatop.openapi.prod-url}")
    private String prodUrl;

    /**
     * Configuration principale de Swagger (OpenAPI).
     * - Définit les serveurs (dev et prod) pour tester les endpoints.
     * - Ajoute les informations générales de l’API : titre, description, version.
     * - Déclare le schéma de sécurité JWT (bearerAuth) pour autoriser les requêtes protégées.
     *
     * @return l'objet OpenAPI configuré.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url(devUrl).description("ChatOp API - Environnement de développement"),
                        new Server().url(prodUrl).description("ChatOp API - Environnement de production")
                ))
                .info(new Info()
                        .title("ChatOp API")
                        .description("ChatOp API est le backend d'une application de gestion de locations immobilières. "
                                + "Il prend en charge la gestion des utilisateurs, des locations et des messages échangés entre les utilisateurs.")
                        .version("1.0")
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                ));
    }
}

/**
 * Classe de configuration Swagger pour la documentation OpenAPI de l’application ChatOp.
 * ➜ Fournit une documentation interactive avec les routes, les schémas et la sécurité JWT.
 * ➜ Facilite les tests de l’API en environnement dev/prod via Swagger UI.
 */

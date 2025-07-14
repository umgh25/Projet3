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
        // URL de l'API en environnement de développement
    @Value("${chatop.openapi.dev-url}")
    private String devUrl;
        // URL de l'API en environnement de production
    @Value("${chatop.openapi.prod-url}")
    private String prodUrl;
        // Configuration de l'OpenAPI pour Swagger
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(new Server().url(devUrl).description("ChatOp API URL in development environment"),
                        new Server().url(prodUrl).description("ChatOp API URL in production environment")))
                .info(new Info()
                        .title("ChatOp API")
                        .description("ChatOp API is the backend for a rental management application. " +
                                "It handles user management, rentals, and messages exchanged between users.")
                        .version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                ;
    }
}
// Cette classe configure Swagger pour documenter l'API de l'application ChatOp.
// Elle définit les informations de base de l'API, les serveurs disponibles et le schéma de sécurité utilisé pour l'authentification.
// Les URLs de l'API en développement et en production sont injectées via des propriétés Spring
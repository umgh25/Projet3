package com.mick.chatop;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatopApplication {

    public static void main(String[] args) {
        // Charger le fichier .env
        Dotenv dotenv = Dotenv.load();

        // Injecter les variables dans l'environnement
        System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
        System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
        System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));

        // Lancer Spring Boot
        SpringApplication.run(ChatopApplication.class, args);
    }
}

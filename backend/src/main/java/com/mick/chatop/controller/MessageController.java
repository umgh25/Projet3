package com.mick.chatop.controller;

import com.mick.chatop.dto.ErrorResponse;
import com.mick.chatop.dto.MessageRequestDto;
import com.mick.chatop.dto.SuccessResponse;
import com.mick.chatop.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour la gestion des messages.
 * 
 * Fournit une API sécurisée pour permettre à un utilisateur authentifié d’envoyer un message
 * à propos d’une annonce (rental). Le message est associé à l’annonce et à l’expéditeur.
 * 
 *
 * URL racine :</b> /api/messages
 */
@Tag(name = "Messages", description = "Message management APIs")
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    /**
     * Constructeur injectant le service de gestion des messages.
     *
     * @param messageService Service métier pour les messages
     */
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Crée un nouveau message associé à une annonce.
     *
     * Sécurité : Requiert un token JWT valide (bearerAuth)
     *
     * Réponses possibles :
     * 
     *     200 - Message envoyé avec succès
     *     400 - Erreur de validation (champ manquant ou invalide)
     *     401 - Utilisateur non authentifié
     * 
     *
     * @param messageRequestDto     Données du message à créer (corps de la requête)
     * @param bindingResult         Résultat de la validation du DTO
     * @param authentication        Contexte d’authentification de Spring Security
     * @return ResponseEntity contenant un message de succès ou une erreur détaillée
     */
    @Operation(
            summary = "Create a new message",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Message created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "SuccessResponse",
                                                    value = """
                            {
                              "message": "Message send with success"
                            }
                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request: Validation error or illegal argument",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "ValidationErrorResponse",
                                                    value = """
                            {
                              "error": "BAD_REQUEST",
                              "status": 400,
                              "message": "rental_id must not be null"
                            }
                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: User not authenticated or runtime error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "UnauthorizedResponse",
                                                    value = """
                            {
                              "error": "UNAUTHORIZED",
                              "status": 401,
                              "message": "Utilisateur non authentifié"
                            }
                            """
                                            )
                                    }
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> createMessage(
            @Valid @RequestBody MessageRequestDto messageRequestDto,
            BindingResult bindingResult,
            Authentication authentication) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .reduce((e1, e2) -> e1 + "; " + e2)
                        .orElse("Validation error");

                return ResponseEntity
                        .badRequest()
                        .body(new ErrorResponse(
                                "BAD_REQUEST",
                                HttpStatus.BAD_REQUEST.value(),
                                errorMessage
                        ));
            }
            messageService.createMessage(messageRequestDto);
            return ResponseEntity.ok(new SuccessResponse("Message sent with success"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            "BAD_REQUEST",
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage()
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ErrorResponse(
                            "UNAUTHORIZED",
                            HttpStatus.UNAUTHORIZED.value(),
                            e.getMessage()
                    )
            );
        }
    }
}

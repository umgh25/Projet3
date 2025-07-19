package com.mick.chatop.controller;

import com.mick.chatop.dto.*;
import com.mick.chatop.service.UserService;
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
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST dédié aux opérations d'authentification.
 * 
 * Gère les actions suivantes :
 * - Connexion utilisateur (/login)
 * - Inscription utilisateur (/register)
 * - Récupération des infos de l'utilisateur authentifié (/me)
 */
@Tag(name = "Authentication", description = "Authentication APIs (Login, Register, Authenticated user info)")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    /**
     * Constructeur pour injecter le service utilisateur.
     *
     * @param userService le service qui contient la logique métier liée aux utilisateurs
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Authentifie un utilisateur et retourne un token JWT.
     *
     * @param request les informations de connexion de l'utilisateur (email, mot de passe)
     * @param result le résultat de la validation du formulaire
     * @return une réponse contenant le token JWT si succès, ou une erreur détaillée sinon
     */
    @Operation(
            summary = "Log in a user and return a JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User logged in successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class),
                                    examples = @ExampleObject(name = "SuccessResponse", value = "{\"token\": \"your_generated_token_here\"}"))),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(name = "InvalidCredentialsResponse", value = """
                                        {
                                          "error": "UNAUTHORIZED",
                                          "status": 401,
                                          "reason": "Authentication failed: invalid credentials."
                                        }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Validation error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(name = "ValidationErrorResponse", value = """
                                        {
                                          "error": "BAD_REQUEST",
                                          "status": 400,
                                          "reason": "Email or password must not be empty."
                                        }
                                    """)))
            })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .reduce((e1, e2) -> e1 + "; " + e2).orElse("Validation error");
            return ResponseEntity.badRequest().body(new ErrorResponse("BAD_REQUEST", 400, errorMessage));
        }

        try {
            return ResponseEntity.ok(userService.login(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("UNAUTHORIZED", 401, "Authentication failed: " + e.getMessage()));
        }
    }

    /**
     * Enregistre un nouvel utilisateur et retourne un token JWT.
     *
     * @param request les informations d'inscription de l'utilisateur (nom, email, mot de passe)
     * @param result le résultat de la validation du formulaire
     * @return une réponse contenant le token JWT si l'inscription réussit, ou une erreur détaillée sinon
     */
    @Operation(
            summary = "Register a new user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class),
                                    examples = @ExampleObject(name = "SuccessResponse", value = "{\"token\": \"your_generated_token_here\"}"))),
                    @ApiResponse(responseCode = "400", description = "Validation or registration error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(name = "ValidationErrorResponse", value = """
                                                {
                                                  "error": "BAD_REQUEST",
                                                  "status": 400,
                                                  "reason": "Email or password must not be empty."
                                                }
                                            """),
                                            @ExampleObject(name = "RegistrationFailureResponse", value = """
                                                {
                                                  "error": "BAD_REQUEST",
                                                  "status": 400,
                                                  "reason": "Registration failed : Already exists."
                                                }
                                            """)
                                    }))
            })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .reduce((e1, e2) -> e1 + "; " + e2).orElse("Validation error");
            return ResponseEntity.badRequest().body(new ErrorResponse("BAD_REQUEST", 400, errorMessage));
        }

        try {
            return ResponseEntity.ok(userService.register(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("BAD_REQUEST", 400, "Registration failed : " + e.getMessage()));
        }
    }

    /**
     * Retourne les informations de l'utilisateur actuellement authentifié.
     *
     * @param authentication l'objet Spring Security contenant les informations de l'utilisateur connecté
     * @return les données de l'utilisateur connecté, ou une erreur 401 si le token est manquant ou invalide
     */
    @Operation(
            summary = "Get the current authenticated user's information",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authenticated user info",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(name = "UnauthorizedResponse", value = """
                                        {
                                          "error": "UNAUTHORIZED",
                                          "status": 401,
                                          "message": "Token missing or invalid"
                                        }
                                    """)))
            })
    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedUser(Authentication authentication) {
        try {
            return ResponseEntity.ok(userService.getAuthenticatedUser(authentication));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("UNAUTHORIZED", 401, e.getMessage()));
        }
    }

    /**
     * Déconnecte l'utilisateur en invalidant le token JWT.
     * 
     * @param authorizationHeader l'en-tête d'autorisation contenant le token JWT
     * @return une réponse indiquant que la déconnexion a réussi, ou une erreur 401 si le token est manquant ou invalide
     */
    @Operation(
            summary = "Logout the current user (invalidate JWT)",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User logged out successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class),
                                    examples = @ExampleObject(name = "SuccessResponse", value = "{\"message\": \"Logged out successfully\"}"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(name = "UnauthorizedResponse", value = "{\"error\": \"UNAUTHORIZED\",\"status\":401,\"message\":\"Token missing or invalid\"}")))
            })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("UNAUTHORIZED", 401, "Token missing or invalid"));
        }
        String token = authorizationHeader.substring(7);
        userService.logout(token);
        return ResponseEntity.ok(new SuccessResponse("Logged out successfully"));
    }
}

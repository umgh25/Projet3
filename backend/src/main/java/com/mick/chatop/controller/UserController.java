package com.mick.chatop.controller;

import com.mick.chatop.dto.ErrorResponse;
import com.mick.chatop.dto.UserDto;
import com.mick.chatop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour la gestion des utilisateurs.
 *
 * Fournit un endpoint permettant de récupérer les informations d'un utilisateur par son ID.
 */
@Tag(name = "User", description = "User management APIs")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    /**
     * Constructeur avec injection du service utilisateur.
     *
     * @param userService le service de gestion des utilisateurs
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Récupère les informations d'un utilisateur à partir de son ID.
     * Nécessite un token JWT valide.
     *
     * @param id l'identifiant de l'utilisateur
     * @return les informations de l'utilisateur si trouvé, ou une erreur 401/404
     */
    @Operation(
            summary = "Get user information by ID",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(name = "UnauthorizedResponse", value = """
                                        {
                                          "error": "UNAUTHORIZED",
                                          "status": 401,
                                          "message": "Token is missing or invalid"
                                        }
                                    """))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(name = "NotFoundResponse", value = """
                                        {
                                          "error": "NOT_FOUND",
                                          "status": 404,
                                          "message": "User not found"
                                        }
                                    """)))
            })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Integer id) {
        try {
            UserDto user = userService.getUserById(id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("NOT_FOUND", 404, "User not found"));
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("UNAUTHORIZED", 401, e.getMessage()));
        }
    }
}

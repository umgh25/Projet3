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

@Tag(name = "User", description = "User and authentication management APIs")
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Log in a user and return a JWT token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User logged in successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "SuccessResponse",
                                                    value = "{\"token\": \"your_generated_token_here\"}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid credentials or login error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "InvalidCredentialsResponse",
                                                    value = """
                        {
                          "error": "UNAUTHORIZED",
                          "status": 401,
                          "reason": "Authentication failed: invalid credentials."
                        }
                        """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request: Validation error",
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
                          "reason": "Email or password must not be empty."
                        }
                        """
                                            )
                                    }
                            )
                    )
            })

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors()
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
        try {
            return ResponseEntity.ok(userService.login(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(
                            "UNAUTHORIZED",
                            HttpStatus.UNAUTHORIZED.value(),
                            "Authentication failed: " + e.getMessage()
                    ));
        }
    }

    @Operation(
            summary = "Register a new user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User registered successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "SuccessResponse",
                                                    value = "{\"token\": \"your_generated_token_here\"}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request: Validation error or registration failure",
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
                          "reason": "Email or password must not be empty."
                        }
                        """
                                            ),
                                            @ExampleObject(
                                                    name = "RegistrationFailureResponse",
                                                    value = """
                        {
                          "error": "BAD_REQUEST",
                          "status": 400,
                          "reason": "Registration failed : Already exists."
                        }
                        """
                                            )
                                    }
                            )
                    )
            })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors()
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
        try {
            return ResponseEntity.ok(userService.register(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(
                            "BAD_REQUEST",
                            HttpStatus.BAD_REQUEST.value(),
                            "Registration failed : " + e.getMessage()
                    ));

        }
    }

    @Operation(
            summary = "Get the current authenticated user's information",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Authenticated user's information returned successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: Authentication is required",
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
                              "message": "Token missing or invalid"
                            }
                            """
                                            )
                                    }
                            )
                    )
            }
    )
    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedUser(Authentication authentication) {
        try {
            return ResponseEntity.ok(userService.getAuthenticatedUser(authentication));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(
                            "UNAUTHORIZED",
                            HttpStatus.UNAUTHORIZED.value(),
                            e.getMessage()
                    ));
        }
    }

    @Operation(
            summary = "Get user information by ID",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: Authentication is required",
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
                              "message": "Token is missing or invalid"
                            }
                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "NotFoundResponse",
                                                    value = """
                            {
                              "error": "NOT_FOUND",
                              "status": 404,
                              "message": "User not found"
                            }
                            """
                                            )
                                    }
                            )
                    )
            }
    )

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Integer id) {
        try {
            UserDto user = userService.getUserById(id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(
                                "NOT_FOUND",
                                HttpStatus.NOT_FOUND.value(),
                                "User not found"
                        ));
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(
                            "UNAUTHORIZED",
                            HttpStatus.UNAUTHORIZED.value(),
                            e.getMessage()
                    ));
        }
    }

}
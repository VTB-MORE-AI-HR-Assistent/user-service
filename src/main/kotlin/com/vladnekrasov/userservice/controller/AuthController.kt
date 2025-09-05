package com.vladnekrasov.userservice.controller

import com.vladnekrasov.userservice.dto.*
import com.vladnekrasov.userservice.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
@Tag(name = "Authentication", description = "Authentication and authorization endpoints")
class AuthController(
    private val authService: AuthService
) {
    
    @PostMapping("/login")
    @Operation(
        summary = "User login",
        description = "Authenticate user with email and password to receive JWT tokens"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully authenticated",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = AuthResponse::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Invalid credentials",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request format",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @SecurityRequirements
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.login(request))
    }
    
    @PostMapping("/register")
    @Operation(
        summary = "Register new user",
        description = "Create a new user account with the provided information"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "User successfully created",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = AuthResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request or email already exists",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "422",
                description = "Validation error",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @SecurityRequirements
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(authService.register(request))
    }
    
    @PostMapping("/refresh")
    @Operation(
        summary = "Refresh access token",
        description = "Get a new access token using a valid refresh token"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Token successfully refreshed",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = TokenResponse::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Invalid or expired refresh token",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request format",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @SecurityRequirements
    fun refresh(@Valid @RequestBody request: RefreshTokenRequest): ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(authService.refreshToken(request))
    }
}

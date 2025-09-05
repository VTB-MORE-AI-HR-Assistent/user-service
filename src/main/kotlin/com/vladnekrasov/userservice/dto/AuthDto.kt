package com.vladnekrasov.userservice.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "Login request with email and password")
data class LoginRequest(
    @field:Email(message = "Email должен быть валидным")
    @field:NotBlank(message = "Email обязателен")
    @Schema(description = "User email address", example = "user@example.com", required = true)
    val email: String,
    
    @field:NotBlank(message = "Пароль обязателен")
    @Schema(description = "User password", example = "password123", required = true)
    val password: String
)

@Schema(description = "User registration request")
data class RegisterRequest(
    @field:Email(message = "Email должен быть валидным")
    @field:NotBlank(message = "Email обязателен")
    @Schema(description = "User email address", example = "newuser@example.com", required = true)
    val email: String,
    
    @field:NotBlank(message = "Пароль обязателен")
    @field:Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    @Schema(description = "User password (minimum 6 characters)", example = "SecurePass123", required = true, minLength = 6)
    val password: String,
    
    @field:NotBlank(message = "Имя обязательно")
    @Schema(description = "User first name", example = "John", required = true)
    val firstName: String,
    
    @field:NotBlank(message = "Фамилия обязательна")
    @Schema(description = "User last name", example = "Doe", required = true)
    val lastName: String
)

@Schema(description = "Authentication response with tokens and user information")
data class AuthResponse(
    @Schema(description = "JWT access token for API authentication", example = "eyJhbGciOiJIUzUxMiJ9...")
    val accessToken: String,
    
    @Schema(description = "Refresh token for obtaining new access tokens", example = "eyJhbGciOiJIUzUxMiJ9...")
    val refreshToken: String,
    
    @Schema(description = "Token type (always 'Bearer')", example = "Bearer")
    val tokenType: String = "Bearer",
    
    @Schema(description = "Access token expiration time in milliseconds", example = "86400000")
    val expiresIn: Long,
    
    @Schema(description = "Authenticated user information")
    val user: UserDto
)

@Schema(description = "Request to refresh access token")
data class RefreshTokenRequest(
    @field:NotBlank(message = "Refresh token обязателен")
    @Schema(description = "Valid refresh token", example = "eyJhbGciOiJIUzUxMiJ9...", required = true)
    val refreshToken: String
)

@Schema(description = "Token refresh response")
data class TokenResponse(
    @Schema(description = "New JWT access token", example = "eyJhbGciOiJIUzUxMiJ9...")
    val accessToken: String,
    
    @Schema(description = "Token type (always 'Bearer')", example = "Bearer")
    val tokenType: String = "Bearer",
    
    @Schema(description = "Access token expiration time in milliseconds", example = "86400000")
    val expiresIn: Long
)

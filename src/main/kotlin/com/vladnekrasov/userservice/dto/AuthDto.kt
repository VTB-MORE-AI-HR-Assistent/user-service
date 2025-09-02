package com.vladnekrasov.userservice.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field:Email(message = "Email должен быть валидным")
    @field:NotBlank(message = "Email обязателен")
    val email: String,
    
    @field:NotBlank(message = "Пароль обязателен")
    val password: String
)

data class RegisterRequest(
    @field:Email(message = "Email должен быть валидным")
    @field:NotBlank(message = "Email обязателен")
    val email: String,
    
    @field:NotBlank(message = "Пароль обязателен")
    @field:Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    val password: String,
    
    @field:NotBlank(message = "Имя обязательно")
    val firstName: String,
    
    @field:NotBlank(message = "Фамилия обязательна")
    val lastName: String
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long,
    val user: UserDto
)

data class RefreshTokenRequest(
    @field:NotBlank(message = "Refresh token обязателен")
    val refreshToken: String
)

data class TokenResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long
)

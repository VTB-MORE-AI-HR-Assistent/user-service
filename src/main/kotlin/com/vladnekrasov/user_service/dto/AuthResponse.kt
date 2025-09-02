package com.vladnekrasov.user_service.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Ответ с JWT токенами")
data class AuthResponse(
    @field:Schema(description = "JWT токен доступа", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val accessToken: String,
    
    @field:Schema(description = "Refresh токен для обновления access токена", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val refreshToken: String,
    
    @field:Schema(description = "Тип токена", example = "Bearer")
    val tokenType: String = "Bearer",
    
    @field:Schema(description = "Время жизни access токена в секундах", example = "3600")
    val expiresIn: Long = 3600
)

package com.vladnekrasov.user_service.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "Запрос на обновление токена")
data class RefreshTokenRequest(
    @field:NotBlank(message = "Refresh токен обязателен")
    @field:Schema(description = "Refresh токен для обновления access токена", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    val refreshToken: String
)

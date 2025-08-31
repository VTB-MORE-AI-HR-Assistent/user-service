package com.vladnekrasov.user_service.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Ответ с токенами аутентификации")
data class AuthResponse(
    @field:Schema(
        description = "Access токен",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    @JsonProperty("access_token")
    val accessToken: String,

    @field:Schema(
        description = "Refresh токен",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    @JsonProperty("refresh_token")
    val refreshToken: String
)

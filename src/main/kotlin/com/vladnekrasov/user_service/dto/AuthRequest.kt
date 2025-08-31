package com.vladnekrasov.user_service.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@Schema(description = "Запрос на аутентификацию")
data class AuthRequest(
    @field:Schema(
        description = "Email пользователя",
        example = "hr@company.com",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    val email: String,

    @field:Schema(
        description = "Пароль пользователя",
        example = "password123",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @field:NotBlank(message = "Password is required")
    val password: String
)

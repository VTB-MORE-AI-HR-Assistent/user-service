package com.vladnekrasov.user_service.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@Schema(description = "Запрос на вход в систему")
data class LoginRequest(
    @field:NotBlank(message = "Email обязателен")
    @field:Email(message = "Некорректный формат email")
    @field:Schema(description = "Email пользователя", example = "ivan@example.com", required = true)
    val email: String,
    
    @field:NotBlank(message = "Пароль обязателен")
    @field:Schema(description = "Пароль пользователя", example = "MyPass123!", required = true)
    val password: String
)

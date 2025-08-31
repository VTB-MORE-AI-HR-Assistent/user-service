package com.vladnekrasov.user_service.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "Запрос на регистрацию нового пользователя")
data class RegisterRequest(
    @field:Schema(
        description = "Имя пользователя",
        example = "Иван",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @field:NotBlank(message = "First name is required")
    @field:Size(max = 100, message = "First name must be less than 100 characters")
    val firstName: String,

    @field:Schema(
        description = "Фамилия пользователя",
        example = "Иванов",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @field:NotBlank(message = "Last name is required")
    @field:Size(max = 100, message = "Last name must be less than 100 characters")
    val lastName: String,

    @field:Schema(
        description = "Email пользователя",
        example = "ivan.ivanov@company.com",
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
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String
)

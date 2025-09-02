package com.vladnekrasov.user_service.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Schema(description = "Запрос на регистрацию пользователя")
data class RegisterRequest(
    @field:NotBlank(message = "Имя обязательно")
    @field:Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    @field:Schema(description = "Имя пользователя", example = "Иван", required = true)
    val firstName: String,
    
    @field:NotBlank(message = "Фамилия обязательна")
    @field:Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
    @field:Schema(description = "Фамилия пользователя", example = "Иванов", required = true)
    val lastName: String,
    
    @field:NotBlank(message = "Email обязателен")
    @field:Email(message = "Некорректный формат email")
    @field:Schema(description = "Email пользователя", example = "ivan@example.com", required = true)
    val email: String,
    
    @field:NotBlank(message = "Пароль обязателен")
    @field:Size(min = 8, max = 100, message = "Пароль должен быть от 8 до 100 символов")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
        message = "Пароль должен содержать минимум одну заглавную букву, одну строчную букву, одну цифру и один специальный символ"
    )
    @field:Schema(description = "Пароль пользователя (минимум 8 символов, должен содержать заглавную и строчную буквы, цифру и спецсимвол)", example = "MyPass123!", required = true)
    val password: String
)

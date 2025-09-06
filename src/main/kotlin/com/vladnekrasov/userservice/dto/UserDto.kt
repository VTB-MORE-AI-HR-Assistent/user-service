package com.vladnekrasov.userservice.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "User information")
data class UserDto(
    @Schema(description = "User ID", example = "1")
    val id: Long,
    
    @Schema(description = "User email address", example = "user@example.com")
    val email: String,
    
    @Schema(description = "User first name", example = "John")
    val firstName: String,
    
    @Schema(description = "User last name", example = "Doe")
    val lastName: String
)

@Schema(description = "Request to create a new user")
data class CreateUserRequest(
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

@Schema(description = "Request to update user information")
data class UpdateUserRequest(
    @field:NotBlank(message = "Имя обязательно")
    @Schema(description = "New first name (optional)", example = "Jane", nullable = true)
    val firstName: String?,
    
    @field:NotBlank(message = "Фамилия обязательна")
    @Schema(description = "New last name (optional)", example = "Smith", nullable = true)
    val lastName: String?
)

@Schema(description = "Request to change user password")
data class ChangePasswordRequest(
    @field:NotBlank(message = "Текущий пароль обязателен")
    @Schema(description = "Current password for verification", example = "OldPassword123", required = true)
    val currentPassword: String,
    
    @field:NotBlank(message = "Новый пароль обязателен")
    @field:Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    @Schema(description = "New password (minimum 6 characters)", example = "NewSecurePass456", required = true, minLength = 6)
    val newPassword: String
)

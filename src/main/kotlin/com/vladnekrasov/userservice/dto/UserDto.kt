package com.vladnekrasov.userservice.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserDto(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String
)

data class CreateUserRequest(
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

data class UpdateUserRequest(
    @field:NotBlank(message = "Имя обязательно")
    val firstName: String?,
    
    @field:NotBlank(message = "Фамилия обязательна")
    val lastName: String?
)

data class ChangePasswordRequest(
    @field:NotBlank(message = "Текущий пароль обязателен")
    val currentPassword: String,
    
    @field:NotBlank(message = "Новый пароль обязателен")
    @field:Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    val newPassword: String
)

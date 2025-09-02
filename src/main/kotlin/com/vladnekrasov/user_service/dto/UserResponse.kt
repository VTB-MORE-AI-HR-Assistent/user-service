package com.vladnekrasov.user_service.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Информация о пользователе")
data class UserResponse(
    @field:Schema(description = "Идентификатор пользователя", example = "1")
    val id: Long,
    
    @field:Schema(description = "Имя пользователя", example = "Иван")
    val firstName: String,
    
    @field:Schema(description = "Фамилия пользователя", example = "Иванов")
    val lastName: String,
    
    @field:Schema(description = "Email пользователя", example = "ivan@example.com")
    val email: String
)

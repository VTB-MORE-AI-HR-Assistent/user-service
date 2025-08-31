package com.vladnekrasov.user_service.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserRequest(
    @field:NotBlank(message = "Имя не может быть пустым")
    @field:Size(max = 100, message = "Имя не может быть длиннее 100 символов")
    val firstName: String,

    @field:NotBlank(message = "Фамилия не может быть пустой")
    @field:Size(max = 100, message = "Фамилия не может быть длиннее 100 символов")
    val lastName: String,

    @field:NotBlank(message = "Email не может быть пустым")
    @field:Email(message = "Некорректный формат email")
    @field:Size(max = 255, message = "Email не может быть длиннее 255 символов")
    val email: String
)

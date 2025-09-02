package com.vladnekrasov.user_service.controller

import com.vladnekrasov.user_service.dto.UserResponse
import com.vladnekrasov.user_service.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "Операции с пользователями")
class UserController(
    private val userService: UserService
) {
    
    @GetMapping("/me")
    @Operation(
        summary = "Получить информацию о текущем пользователе",
        description = "Возвращает информацию о пользователе на основе JWT токена",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Успешно получена информация о пользователе"),
            ApiResponse(responseCode = "401", description = "Отсутствует или невалидный JWT токен"),
            ApiResponse(responseCode = "404", description = "Пользователь не найден")
        ]
    )
    fun getCurrentUser(authentication: Authentication): ResponseEntity<UserResponse> {
        val email = authentication.name
        return ResponseEntity.ok(userService.getUserByEmail(email))
    }
}

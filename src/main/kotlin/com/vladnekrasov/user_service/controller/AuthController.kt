package com.vladnekrasov.user_service.controller

import com.vladnekrasov.user_service.dto.*
import com.vladnekrasov.user_service.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Аутентификация и регистрация пользователей")
class AuthController(
    private val authService: AuthService
) {
    
    @PostMapping("/register")
    @Operation(
        summary = "Регистрация нового пользователя",
        description = "Создает нового пользователя и возвращает JWT токены"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Успешная регистрация"),
            ApiResponse(responseCode = "400", description = "Некорректные данные или пользователь уже существует"),
            ApiResponse(responseCode = "422", description = "Ошибка валидации данных")
        ]
    )
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.register(request))
    }
    
    @PostMapping("/login")
    @Operation(
        summary = "Вход в систему",
        description = "Аутентификация пользователя по email и паролю, возвращает JWT токены"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Успешная аутентификация"),
            ApiResponse(responseCode = "401", description = "Неверный email или пароль"),
            ApiResponse(responseCode = "422", description = "Ошибка валидации данных")
        ]
    )
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.login(request))
    }
    
    @PostMapping("/refresh")
    @Operation(
        summary = "Обновление токена доступа",
        description = "Обновляет access токен используя refresh токен"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Токены успешно обновлены"),
            ApiResponse(responseCode = "401", description = "Невалидный refresh токен"),
            ApiResponse(responseCode = "422", description = "Ошибка валидации данных")
        ]
    )
    fun refresh(@Valid @RequestBody request: RefreshTokenRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.refresh(request))
    }
}

package com.vladnekrasov.user_service.controller

import com.vladnekrasov.user_service.dto.AuthRequest
import com.vladnekrasov.user_service.dto.AuthResponse
import com.vladnekrasov.user_service.dto.RegisterRequest
import com.vladnekrasov.user_service.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "API для аутентификации и авторизации")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Регистрация нового пользователя",
        description = """
        Создает новый аккаунт HR специалиста в системе.
        
        Требования к паролю:
        - Минимум 8 символов
        - Должен содержать буквы и цифры
        
        Возвращает:
        - JWT access токен (срок действия: 24 часа)
        - JWT refresh токен (срок действия: 7 дней)
        - Информацию о пользователе
        """
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "201",
            description = "Пользователь успешно зарегистрирован",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Успешная регистрация",
                    value = """
                    {
                      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                      "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                      "tokenType": "Bearer",
                      "expiresIn": 86400,
                      "user": {
                        "id": 1,
                        "firstName": "Иван",
                        "lastName": "Иванов",
                        "email": "ivan.ivanov@company.com"
                      }
                    }
                    """
                )]
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Неверные данные запроса (валидация не пройдена)",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Ошибка валидации",
                    value = """
                    {
                      "timestamp": "2024-01-01T12:00:00.000Z",
                      "status": 400,
                      "error": "Bad Request",
                      "message": "Validation failed",
                      "errors": [
                        {
                          "field": "email",
                          "message": "Некорректный формат email"
                        },
                        {
                          "field": "password",
                          "message": "Пароль должен содержать минимум 8 символов"
                        }
                      ]
                    }
                    """
                )]
            )]
        ),
        ApiResponse(
            responseCode = "409",
            description = "Пользователь с таким email уже существует",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Email уже занят",
                    value = """
                    {
                      "timestamp": "2024-01-01T12:00:00.000Z",
                      "status": 409,
                      "error": "Conflict",
                      "message": "Пользователь с email ivan.ivanov@company.com уже существует"
                    }
                    """
                )]
            )]
        )
    ])
    fun register(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для регистрации нового пользователя",
            required = true,
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Пример регистрации",
                    value = """
                    {
                      "firstName": "Иван",
                      "lastName": "Иванов",
                      "email": "ivan.ivanov@company.com",
                      "password": "password123"
                    }
                    """
                )]
            )]
        )
        @Valid @RequestBody request: RegisterRequest
    ): AuthResponse {
        return authService.register(request)
    }

    @PostMapping("/login")
    @Operation(
        summary = "Аутентификация пользователя",
        description = """
        Выполняет вход в систему по email и паролю.
        
        Возвращает:
        - JWT access токен для авторизации API запросов
        - JWT refresh токен для обновления access токена
        - Информацию о пользователе
        
        Использование токена:
        Полученный access токен нужно передавать в заголовке Authorization:
        Authorization: Bearer <access_token>
        """
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Успешная аутентификация",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Успешный вход",
                    value = """
                    {
                      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                      "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                      "tokenType": "Bearer",
                      "expiresIn": 86400,
                      "user": {
                        "id": 1,
                        "firstName": "Иван",
                        "lastName": "Иванов",
                        "email": "ivan.ivanov@company.com"
                      }
                    }
                    """
                )]
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Неверные данные запроса (валидация не пройдена)",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Ошибка валидации",
                    value = """
                    {
                      "timestamp": "2024-01-01T12:00:00.000Z",
                      "status": 400,
                      "error": "Bad Request",
                      "message": "Validation failed",
                      "errors": [
                        {
                          "field": "email",
                          "message": "Email не может быть пустым"
                        }
                      ]
                    }
                    """
                )]
            )]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Неверные учетные данные",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Неверный пароль",
                    value = """
                    {
                      "timestamp": "2024-01-01T12:00:00.000Z",
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "Неверные учетные данные"
                    }
                    """
                )]
            )]
        )
    ])
    fun login(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Учетные данные пользователя для входа в систему",
            required = true,
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Пример входа",
                    value = """
                    {
                      "email": "ivan.ivanov@company.com",
                      "password": "password123"
                    }
                    """
                )]
            )]
        )
        @Valid @RequestBody request: AuthRequest
    ): AuthResponse {
        return authService.authenticate(request)
    }

    @PostMapping("/refresh-token")
    @Operation(
        summary = "Обновление access токена",
        description = """
        Обновляет истекший access токен используя refresh токен.
        
        Как использовать:
        1. Отправьте refresh токен в заголовке Authorization
        2. Получите новый access токен в ответе
        
        Заголовки:
        - Authorization: Bearer <refresh_token>
        
        Возвращает:
        - Новый access токен
        - Новый refresh токен
        """
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Токен успешно обновлен",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Успешное обновление",
                    value = """
                    {
                      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                      "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                      "tokenType": "Bearer",
                      "expiresIn": 86400
                    }
                    """
                )]
            )]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Неверный или истекший refresh токен",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Неверный токен",
                    value = """
                    {
                      "timestamp": "2024-01-01T12:00:00.000Z",
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "Refresh токен недействителен или истек"
                    }
                    """
                )]
            )]
        )
    ])
    fun refreshToken(
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        authService.refreshToken(request, response)
    }
}

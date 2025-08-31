package com.vladnekrasov.user_service.controller

import com.vladnekrasov.user_service.dto.UserRequest
import com.vladnekrasov.user_service.dto.UserResponse
import com.vladnekrasov.user_service.dto.UserShortResponse
import com.vladnekrasov.user_service.mapper.UserMapper
import com.vladnekrasov.user_service.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "API для управления пользователями")
@SecurityRequirement(name = "Bearer Authentication")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    @Operation(
        summary = "Получить всех пользователей",
        description = """
        Возвращает список всех пользователей в кратком формате.
        
        Требуется авторизация:
        Передайте JWT токен в заголовке: Authorization: Bearer <access_token>
        """
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Список пользователей получен успешно",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Список пользователей",
                    value = """
                    [
                      {
                        "id": 1,
                        "firstName": "Иван",
                        "lastName": "Иванов",
                        "email": "ivan.ivanov@company.com"
                      },
                      {
                        "id": 2,
                        "firstName": "Мария",
                        "lastName": "Петрова",
                        "email": "maria.petrova@company.com"
                      }
                    ]
                    """
                )]
            )]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Не авторизован - отсутствует или неверный JWT токен",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Ошибка авторизации",
                    value = """
                    {
                      "timestamp": "2024-01-01T12:00:00.000Z",
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "JWT токен отсутствует или недействителен"
                    }
                    """
                )]
            )]
        )
    ])
    fun getAllUsers(): List<UserShortResponse> {
        return userService.getAllUsers().map { UserMapper.toShortResponse(it) }
    }

    @GetMapping("/me")
    @Operation(
        summary = "Получить профиль текущего пользователя",
        description = """
        Возвращает полную информацию о текущем авторизованном пользователе.
        
        Требуется авторизация:
        Передайте JWT токен в заголовке: Authorization: Bearer <access_token>
        """
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Профиль пользователя получен успешно",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Профиль пользователя",
                    value = """
                    {
                      "id": 1,
                      "firstName": "Иван",
                      "lastName": "Иванов",
                      "email": "ivan.ivanov@company.com",
                      "createdAt": "2024-01-01T10:00:00.000Z",
                      "updatedAt": "2024-01-01T10:00:00.000Z"
                    }
                    """
                )]
            )]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Не авторизован - отсутствует или неверный JWT токен",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Ошибка авторизации",
                    value = """
                    {
                      "timestamp": "2024-01-01T12:00:00.000Z",
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "JWT токен отсутствует или недействителен"
                    }
                    """
                )]
            )]
        )
    ])
    fun getCurrentUser(authentication: Authentication): UserResponse {
        return UserMapper.toResponse(userService.getCurrentUser(authentication.name))
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Получить пользователя по ID",
        description = """
        Возвращает полную информацию о пользователе по его идентификатору.
        
        Требуется авторизация:
        Передайте JWT токен в заголовке: Authorization: Bearer <access_token>
        """
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Пользователь найден",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Пользователь найден",
                    value = """
                    {
                      "id": 1,
                      "firstName": "Иван",
                      "lastName": "Иванов",
                      "email": "ivan.ivanov@company.com",
                      "createdAt": "2024-01-01T10:00:00.000Z",
                      "updatedAt": "2024-01-01T10:00:00.000Z"
                    }
                    """
                )]
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Пользователь с указанным ID не найден",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Пользователь не найден",
                    value = """
                    {
                      "timestamp": "2024-01-01T12:00:00.000Z",
                      "status": 404,
                      "error": "Not Found",
                      "message": "Пользователь с ID 999 не найден"
                    }
                    """
                )]
            )]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Не авторизован - отсутствует или неверный JWT токен",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Ошибка авторизации",
                    value = """
                    {
                      "timestamp": "2024-01-01T12:00:00.000Z",
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "JWT токен отсутствует или недействителен"
                    }
                    """
                )]
            )]
        )
    ])
    fun getUserById(
        @Parameter(
            description = "ID пользователя",
            required = true,
            example = "1"
        )
        @PathVariable id: Long
    ): UserResponse {
        return UserMapper.toResponse(userService.getUserById(id))
    }

    @PutMapping("/me")
    @Operation(
        summary = "Обновить профиль текущего пользователя",
        description = """
        Обновляет информацию о текущем авторизованном пользователе.
        
        Требуется авторизация:
        Передайте JWT токен в заголовке: Authorization: Bearer <access_token>
        
        Поля для обновления:
        - firstName: имя пользователя (2-50 символов)
        - lastName: фамилия пользователя (2-50 символов)
        - email: email адрес (валидный формат)
        """
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Профиль пользователя обновлен успешно",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Обновленный профиль",
                    value = """
                    {
                      "id": 1,
                      "firstName": "Иван",
                      "lastName": "Петров",
                      "email": "ivan.petrov@company.com",
                      "createdAt": "2024-01-01T10:00:00.000Z",
                      "updatedAt": "2024-01-01T12:30:00.000Z"
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
                          "field": "firstName",
                          "message": "Имя должно содержать от 2 до 50 символов"
                        }
                      ]
                    }
                    """
                )]
            )]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Не авторизован - отсутствует или неверный JWT токен",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Ошибка авторизации",
                    value = """
                    {
                      "timestamp": "2024-01-01T12:00:00.000Z",
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "JWT токен отсутствует или недействителен"
                    }
                    """
                )]
            )]
        )
    ])
    fun updateCurrentUser(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для обновления профиля пользователя",
            required = true,
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Обновление профиля",
                    value = """
                    {
                      "firstName": "Иван",
                      "lastName": "Петров",
                      "email": "ivan.petrov@company.com"
                    }
                    """
                )]
            )]
        )
        @Valid @RequestBody request: UserRequest,
        authentication: Authentication
    ): UserResponse {
        val currentUser = userService.getCurrentUser(authentication.name)
        val updatedUser = userService.updateUser(currentUser.id!!, request)
        return UserMapper.toResponse(updatedUser)
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Обновить пользователя по ID",
        description = """
        Обновляет информацию о пользователе по его идентификатору.
        
        Требуется авторизация:
        Передайте JWT токен в заголовке: Authorization: Bearer <access_token>
        """
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Пользователь успешно обновлен",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Обновленный пользователь",
                    value = """
                    {
                      "id": 1,
                      "firstName": "Иван",
                      "lastName": "Сидоров",
                      "email": "ivan.sidorov@company.com",
                      "createdAt": "2024-01-01T10:00:00.000Z",
                      "updatedAt": "2024-01-01T14:00:00.000Z"
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
                          "message": "Неверный формат email"
                        }
                      ]
                    }
                    """
                )]
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Пользователь с указанным ID не найден",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Пользователь не найден",
                    value = """
                    {
                      "timestamp": "2024-01-01T12:00:00.000Z",
                      "status": 404,
                      "error": "Not Found",
                      "message": "Пользователь с ID 999 не найден"
                    }
                    """
                )]
            )]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Не авторизован - отсутствует или неверный JWT токен",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Ошибка авторизации",
                    value = """
                    {
                      "timestamp": "2024-01-01T12:00:00.000Z",
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "JWT токен отсутствует или недействителен"
                    }
                    """
                )]
            )]
        )
    ])
    fun updateUser(
        @Parameter(description = "ID пользователя для обновления", example = "1", required = true)
        @PathVariable id: Long,

        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Новые данные пользователя",
            required = true,
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Обновление пользователя",
                    value = """
                    {
                      "firstName": "Иван",
                      "lastName": "Сидоров",
                      "email": "ivan.sidorov@company.com"
                    }
                    """
                )]
            )]
        )
        @Valid @RequestBody request: UserRequest
    ): UserResponse {
        val updatedUser = userService.updateUser(id, request)
        return UserMapper.toResponse(updatedUser)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Удалить пользователя по ID",
        description = """
        Удаляет пользователя по его идентификатору.
        
        Требуется авторизация:
        Передайте JWT токен в заголовке: Authorization: Bearer <access_token>
        """
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "204",
            description = "Пользователь успешно удален"
        ),
        ApiResponse(
            responseCode = "404",
            description = "Пользователь с указанным ID не найден",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Пользователь не найден",
                    value = """
                    {
                      "timestamp": "2024-01-01T12:00:00.000Z",
                      "status": 404,
                      "error": "Not Found",
                      "message": "Пользователь с ID 999 не найден"
                    }
                    """
                )]
            )]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Не авторизован - отсутствует или неверный JWT токен",
            content = [Content(
                mediaType = "application/json",
                examples = [ExampleObject(
                    name = "Ошибка авторизации",
                    value = """
                    {
                      "timestamp": "2024-01-01T12:00:00.000Z",
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "JWT токен отсутствует или недействителен"
                    }
                    """
                )]
            )]
        )
    ])
    fun deleteUser(
        @Parameter(description = "ID пользователя для удаления", example = "1", required = true)
        @PathVariable id: Long
    ) {
        userService.deleteUser(id)
    }
}

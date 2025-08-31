package com.vladnekrasov.user_service.mapper

import com.vladnekrasov.user_service.dto.UserRequest
import com.vladnekrasov.user_service.dto.UserResponse
import com.vladnekrasov.user_service.dto.UserShortResponse
import com.vladnekrasov.user_service.model.User
import java.time.LocalDateTime

object UserMapper {
    
    fun toResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id!!,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
    
    fun toShortResponse(user: User): UserShortResponse {
        return UserShortResponse(
            id = user.id!!,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email
        )
    }
    
    fun updateEntity(user: User, request: UserRequest): User {
        return user.copy(
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            updatedAt = LocalDateTime.now()
        )
    }
}

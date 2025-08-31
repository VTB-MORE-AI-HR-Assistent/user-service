package com.vladnekrasov.user_service.service

import com.vladnekrasov.user_service.dto.UserRequest
import com.vladnekrasov.user_service.model.User

interface UserService {
    fun getAllUsers(): List<User>
    fun getUserById(id: Long): User
    fun updateUser(id: Long, request: UserRequest): User
    fun deleteUser(id: Long)
    fun getCurrentUser(email: String): User
}

package com.vladnekrasov.user_service.service

import com.vladnekrasov.user_service.dto.UserResponse

interface UserService {
    fun getAllUsers(): List<UserResponse>
    fun getUserById(id: Long): UserResponse
    fun getUserByEmail(email: String): UserResponse
    fun deleteUser(id: Long)
}

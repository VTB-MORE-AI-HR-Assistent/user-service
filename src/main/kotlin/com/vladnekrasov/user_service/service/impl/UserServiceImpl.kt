package com.vladnekrasov.user_service.service.impl

import com.vladnekrasov.user_service.dto.UserRequest
import com.vladnekrasov.user_service.exception.ResourceNotFoundException
import com.vladnekrasov.user_service.mapper.UserMapper
import com.vladnekrasov.user_service.model.User
import com.vladnekrasov.user_service.repository.UserRepository
import com.vladnekrasov.user_service.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    override fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    override fun getUserById(id: Long): User {
        return userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Пользователь с ID $id не найден") }
    }

    override fun updateUser(id: Long, request: UserRequest): User {
        val existingUser = getUserById(id)
        val updatedUser = UserMapper.updateEntity(existingUser, request)
        return userRepository.save(updatedUser)
    }

    override fun deleteUser(id: Long) {
        val user = getUserById(id)
        userRepository.delete(user)
    }

    override fun getCurrentUser(email: String): User {
        return userRepository.findByEmail(email)
            .orElseThrow { ResourceNotFoundException("Пользователь с email $email не найден") }
    }
}

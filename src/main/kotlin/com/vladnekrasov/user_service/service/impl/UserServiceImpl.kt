package com.vladnekrasov.user_service.service.impl

import com.vladnekrasov.user_service.dto.UserResponse
import com.vladnekrasov.user_service.exception.UserNotFoundException
import com.vladnekrasov.user_service.repository.UserRepository
import com.vladnekrasov.user_service.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    
    override fun getAllUsers(): List<UserResponse> {
        return userRepository.findAll().map { user ->
            UserResponse(
                id = user.id!!,
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email
            )
        }
    }
    
    override fun getUserById(id: Long): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User with id $id not found") }
        
        return UserResponse(
            id = user.id!!,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email
        )
    }
    
    override fun getUserByEmail(email: String): UserResponse {
        val user = userRepository.findByEmail(email)
            .orElseThrow { UserNotFoundException("User with email $email not found") }
        
        return UserResponse(
            id = user.id!!,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email
        )
    }
    
    override fun deleteUser(id: Long) {
        if (!userRepository.existsById(id)) {
            throw UserNotFoundException("User with id $id not found")
        }
        userRepository.deleteById(id)
    }
}

package com.vladnekrasov.userservice.service

import com.vladnekrasov.userservice.dto.*
import com.vladnekrasov.userservice.entity.User
import com.vladnekrasov.userservice.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    
    fun findAll(pageable: Pageable): Page<UserDto> {
        return userRepository.findAll(pageable).map { it.toDto() }
    }
    
    fun findById(id: Long): UserDto {
        val user = userRepository.findById(id)
            .orElseThrow { NoSuchElementException("Пользователь с id $id не найден") }
        return user.toDto()
    }
    
    fun findByEmail(email: String): User {
        return userRepository.findByEmail(email)
            .orElseThrow { NoSuchElementException("Пользователь с email $email не найден") }
    }
    
    fun create(request: CreateUserRequest): UserDto {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Пользователь с email ${request.email} уже существует")
        }
        
        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            firstName = request.firstName,
            lastName = request.lastName
        )
        
        val savedUser = userRepository.save(user)
        return savedUser.toDto()
    }
    
    fun update(id: Long, request: UpdateUserRequest): UserDto {
        val user = userRepository.findById(id)
            .orElseThrow { NoSuchElementException("Пользователь с id $id не найден") }
        
        val updatedUser = user.copy(
            firstName = request.firstName ?: user.firstName,
            lastName = request.lastName ?: user.lastName
        )
        
        val savedUser = userRepository.save(updatedUser)
        return savedUser.toDto()
    }
    
    fun changePassword(email: String, request: ChangePasswordRequest) {
        val user = findByEmail(email)
        
        if (!passwordEncoder.matches(request.currentPassword, user.password)) {
            throw IllegalArgumentException("Неверный текущий пароль")
        }
        
        val updatedUser = user.copy(
            password = passwordEncoder.encode(request.newPassword)
        )
        
        userRepository.save(updatedUser)
    }
    
    fun delete(id: Long) {
        if (!userRepository.existsById(id)) {
            throw NoSuchElementException("Пользователь с id $id не найден")
        }
        userRepository.deleteById(id)
    }
    
    fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }
    
    private fun User.toDto(): UserDto {
        return UserDto(
            id = this.id,
            email = this.email,
            firstName = this.firstName,
            lastName = this.lastName
        )
    }
}

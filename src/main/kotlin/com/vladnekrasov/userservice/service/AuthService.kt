package com.vladnekrasov.userservice.service

import com.vladnekrasov.userservice.dto.*
import com.vladnekrasov.userservice.entity.User
import com.vladnekrasov.userservice.repository.UserRepository
import com.vladnekrasov.userservice.security.JwtUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtils: JwtUtils
) {
    
    @Value("\${jwt.expiration}")
    private var jwtExpiration: Long = 0
    
    fun login(request: LoginRequest): AuthResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        
        SecurityContextHolder.getContext().authentication = authentication
        
        val user = userRepository.findByEmail(request.email)
            .orElseThrow { NoSuchElementException("Пользователь не найден") }
        
        val claims = mapOf(
            "userId" to user.id,
            "email" to user.email
        )
        
        val accessToken = jwtUtils.generateToken(user.email, claims)
        val refreshToken = jwtUtils.generateRefreshToken(user.email)
        
        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = jwtExpiration,
            user = user.toDto()
        )
    }
    
    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email уже зарегистрирован")
        }
        
        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            firstName = request.firstName,
            lastName = request.lastName
        )
        
        val savedUser = userRepository.save(user)
        
        val claims = mapOf(
            "userId" to savedUser.id,
            "email" to savedUser.email
        )
        
        val accessToken = jwtUtils.generateToken(savedUser.email, claims)
        val refreshToken = jwtUtils.generateRefreshToken(savedUser.email)
        
        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = jwtExpiration,
            user = savedUser.toDto()
        )
    }
    
    fun refreshToken(request: RefreshTokenRequest): TokenResponse {
        try {
            val username = jwtUtils.getUsernameFromToken(request.refreshToken)
            val user = userRepository.findByEmail(username)
                .orElseThrow { NoSuchElementException("Пользователь не найден") }
            
            val claims = mapOf(
                "userId" to user.id,
                "email" to user.email
            )
            
            val newAccessToken = jwtUtils.generateToken(user.email, claims)
            
            return TokenResponse(
                accessToken = newAccessToken,
                expiresIn = jwtExpiration
            )
        } catch (e: Exception) {
            throw IllegalArgumentException("Невалидный refresh token")
        }
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

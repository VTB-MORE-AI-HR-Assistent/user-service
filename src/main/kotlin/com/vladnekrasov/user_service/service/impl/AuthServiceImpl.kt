package com.vladnekrasov.user_service.service.impl

import com.vladnekrasov.user_service.dto.*
import com.vladnekrasov.user_service.exception.EmailAlreadyExistsException
import com.vladnekrasov.user_service.exception.InvalidCredentialsException
import com.vladnekrasov.user_service.model.User
import com.vladnekrasov.user_service.repository.UserRepository
import com.vladnekrasov.user_service.service.AuthService
import com.vladnekrasov.user_service.util.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) : AuthService {
    
    @Value("\${jwt.expiration}")
    private var expiration: Long = 0
    
    override fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw EmailAlreadyExistsException("Email ${request.email} is already registered")
        }
        
        val user = User(
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            password = passwordEncoder.encode(request.password)
        )
        
        val savedUser = userRepository.save(user)
        
        val accessToken = jwtUtil.generateToken(savedUser.email)
        val refreshToken = jwtUtil.generateRefreshToken(savedUser.email)
        
        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = expiration
        )
    }
    
    override fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email)
            .orElseThrow { InvalidCredentialsException("Invalid email or password") }
        
        if (!passwordEncoder.matches(request.password, user.password)) {
            throw InvalidCredentialsException("Invalid email or password")
        }
        
        val accessToken = jwtUtil.generateToken(user.email)
        val refreshToken = jwtUtil.generateRefreshToken(user.email)
        
        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = expiration
        )
    }
    
    override fun refresh(request: RefreshTokenRequest): AuthResponse {
        val email = jwtUtil.extractEmail(request.refreshToken)
        
        if (!jwtUtil.validateToken(request.refreshToken, email)) {
            throw InvalidCredentialsException("Invalid refresh token")
        }
        
        val newAccessToken = jwtUtil.generateToken(email)
        val newRefreshToken = jwtUtil.generateRefreshToken(email)
        
        return AuthResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
            expiresIn = expiration
        )
    }
}

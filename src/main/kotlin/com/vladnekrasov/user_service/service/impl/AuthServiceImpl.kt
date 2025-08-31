package com.vladnekrasov.user_service.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.vladnekrasov.user_service.dto.AuthRequest
import com.vladnekrasov.user_service.dto.AuthResponse
import com.vladnekrasov.user_service.dto.RegisterRequest
import com.vladnekrasov.user_service.exception.UserAlreadyExistsException
import com.vladnekrasov.user_service.exception.UserNotFoundException
import com.vladnekrasov.user_service.model.Token
import com.vladnekrasov.user_service.model.TokenType
import com.vladnekrasov.user_service.model.User
import com.vladnekrasov.user_service.repository.TokenRepository
import com.vladnekrasov.user_service.repository.UserRepository
import com.vladnekrasov.user_service.security.JwtService
import com.vladnekrasov.user_service.security.UserDetailsImpl
import com.vladnekrasov.user_service.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException

@Service
@Transactional
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) : AuthService {

    override fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw UserAlreadyExistsException("User with email ${request.email} already exists")
        }

        val user = User(
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            password = passwordEncoder.encode(request.password)
        )

        val savedUser = userRepository.save(user)
        val userDetails = UserDetailsImpl(savedUser)
        val jwtToken = jwtService.generateToken(userDetails)
        val refreshToken = jwtService.generateRefreshToken(userDetails)

        saveUserToken(savedUser, jwtToken)

        return AuthResponse(jwtToken, refreshToken)
    }

    override fun authenticate(request: AuthRequest): AuthResponse {
        val authInputToken = UsernamePasswordAuthenticationToken(
            request.email,
            request.password
        )
        authenticationManager.authenticate(authInputToken)
        
        val user = getUser(request.email)
        val userDetails = UserDetailsImpl(user)
        val jwtToken = jwtService.generateToken(userDetails)
        val refreshToken = jwtService.generateRefreshToken(userDetails)

        revokeAllUserTokens(user)
        saveUserToken(user, jwtToken)

        return AuthResponse(jwtToken, refreshToken)
    }

    override fun refreshToken(request: HttpServletRequest, response: HttpServletResponse) {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        val refreshToken: String
        val userEmail: String

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return
        }

        refreshToken = authHeader.substring(7)
        userEmail = jwtService.extractUsername(refreshToken)

        if (userEmail.isNotEmpty()) {
            val user = getUser(userEmail)
            val userDetails = UserDetailsImpl(user)
            
            if (jwtService.isRefreshTokenValid(refreshToken, userDetails)) {
                val accessToken = jwtService.generateToken(userDetails)

                revokeAllUserTokens(user)
                saveUserToken(user, accessToken)

                val authResponse = AuthResponse(accessToken, refreshToken)
                writeToResponse(response, authResponse)
            }
        }
    }

    private fun writeToResponse(response: HttpServletResponse, authResponse: AuthResponse) {
        try {
            ObjectMapper().writeValue(response.outputStream, authResponse)
        } catch (e: IOException) {
            throw RuntimeException("Error writing response: ${e.message}")
        }
    }

    private fun getUser(email: String): User {
        return userRepository.findByEmail(email)
            .orElseThrow { UserNotFoundException("User with email $email not found") }
    }

    private fun revokeAllUserTokens(user: User) {
        val validUserTokens = tokenRepository.findAllValidTokensByUser(user.id!!)
        if (validUserTokens.isEmpty()) {
            return
        }
        validUserTokens.forEach { token ->
            token.expired = true
            token.revoked = true
        }
        tokenRepository.saveAll(validUserTokens)
    }

    private fun saveUserToken(user: User, jwtToken: String) {
        val token = Token(
            user = user,
            token = jwtToken,
            tokenType = TokenType.BEARER,
            expired = false,
            revoked = false
        )
        tokenRepository.save(token)
    }
}

package com.vladnekrasov.user_service.service

import com.vladnekrasov.user_service.dto.AuthRequest
import com.vladnekrasov.user_service.dto.AuthResponse
import com.vladnekrasov.user_service.dto.RegisterRequest
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

interface AuthService {
    fun register(request: RegisterRequest): AuthResponse
    fun authenticate(request: AuthRequest): AuthResponse
    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse)
}

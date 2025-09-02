package com.vladnekrasov.user_service.service

import com.vladnekrasov.user_service.dto.*

interface AuthService {
    fun register(request: RegisterRequest): AuthResponse
    fun login(request: LoginRequest): AuthResponse
    fun refresh(request: RefreshTokenRequest): AuthResponse
}

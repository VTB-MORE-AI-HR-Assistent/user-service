package com.vladnekrasov.user_service.dto

data class UserShortResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String
)

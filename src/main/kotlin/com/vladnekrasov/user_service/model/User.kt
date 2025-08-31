package com.vladnekrasov.user_service.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users", schema = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "first_name", nullable = false, length = 100)
    val firstName: String,

    @Column(name = "last_name", nullable = false, length = 100)
    val lastName: String,

    @Column(name = "email", nullable = false, unique = true, length = 255)
    val email: String,

    @Column(name = "password", nullable = false, length = 255)
    val password: String,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

package com.vladnekrasov.user_service.model

import jakarta.persistence.*

@Entity
@Table(name = "tokens", schema = "users")
data class Token(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "token", nullable = false, columnDefinition = "TEXT")
    val token: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", nullable = false)
    val tokenType: TokenType = TokenType.BEARER,

    @Column(name = "expired", nullable = false)
    var expired: Boolean = false,

    @Column(name = "revoked", nullable = false)
    var revoked: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User
)

enum class TokenType {
    BEARER
}

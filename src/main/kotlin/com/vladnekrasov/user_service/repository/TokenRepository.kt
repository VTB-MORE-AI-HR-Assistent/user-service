package com.vladnekrasov.user_service.repository

import com.vladnekrasov.user_service.model.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TokenRepository : JpaRepository<Token, Long> {
    
    @Query("""
        SELECT t FROM Token t 
        WHERE t.user.id = :userId 
        AND (t.expired = false OR t.revoked = false)
    """)
    fun findAllValidTokensByUser(@Param("userId") userId: Long): List<Token>
    
    fun findByToken(token: String): Token?
}

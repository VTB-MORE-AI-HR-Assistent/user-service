package com.vladnekrasov.user_service.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtUtil {
    
    @Value("\${jwt.secret}")
    private lateinit var secret: String
    
    @Value("\${jwt.expiration}")
    private var expiration: Long = 0
    
    @Value("\${jwt.refresh-expiration}")
    private var refreshExpiration: Long = 0
    
    private fun getSigningKey(): Key {
        return Keys.hmacShaKeyFor(secret.toByteArray())
    }
    
    fun generateToken(email: String): String {
        return createToken(email, expiration)
    }
    
    fun generateRefreshToken(email: String): String {
        return createToken(email, refreshExpiration)
    }
    
    private fun createToken(email: String, expiration: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)
        
        return Jwts.builder()
            .subject(email)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()
    }
    
    fun extractEmail(token: String): String {
        return extractClaim(token) { it.subject }
    }
    
    fun extractExpiration(token: String): Date {
        return extractClaim(token) { it.expiration }
    }
    
    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }
    
    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secret.toByteArray()))
            .build()
            .parseSignedClaims(token)
            .payload
    }
    
    fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }
    
    fun validateToken(token: String, email: String): Boolean {
        val tokenEmail = extractEmail(token)
        return (tokenEmail == email && !isTokenExpired(token))
    }
    
    fun validateToken(token: String): Boolean {
        return try {
            !isTokenExpired(token)
        } catch (e: Exception) {
            false
        }
    }
}

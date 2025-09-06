package com.vladnekrasov.userservice.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtils {
    
    @Value("\${jwt.secret}")
    private lateinit var secret: String
    
    @Value("\${jwt.expiration}")
    private var expiration: Long = 0
    
    @Value("\${jwt.refresh-expiration}")
    private var refreshExpiration: Long = 0
    
    private fun getSigningKey(): SecretKey {
        val keyBytes = secret.toByteArray()
        return Keys.hmacShaKeyFor(keyBytes)
    }
    
    fun generateToken(username: String, claims: Map<String, Any> = emptyMap()): String {
        val createdDate = Date()
        val expirationDate = Date(createdDate.time + expiration)
        
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(createdDate)
            .setExpiration(expirationDate)
            .signWith(getSigningKey(), SignatureAlgorithm.HS512)
            .compact()
    }
    
    fun generateRefreshToken(username: String): String {
        val createdDate = Date()
        val expirationDate = Date(createdDate.time + refreshExpiration)
        
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(createdDate)
            .setExpiration(expirationDate)
            .signWith(getSigningKey(), SignatureAlgorithm.HS512)
            .compact()
    }
    
    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return username == userDetails.username && !isTokenExpired(token)
    }
    
    fun getUsernameFromToken(token: String): String {
        return getClaimsFromToken(token).subject
    }
    
    fun getExpirationDateFromToken(token: String): Date {
        return getClaimsFromToken(token).expiration
    }
    
    fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }
    
    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }
}

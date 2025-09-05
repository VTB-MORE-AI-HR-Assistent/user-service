package com.vladnekrasov.userservice.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.slf4j.LoggerFactory

@Component
class JwtAuthenticationFilter(
    private val jwtUtils: JwtUtils,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {
    
    private val logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        logger.info("JWT Filter: Processing request ${request.method} ${request.requestURI}")
        logger.info("JWT Filter: Headers: ${request.headerNames.asSequence().map { "$it: ${request.getHeader(it)}" }.joinToString(", ")}")
        
        val token = extractToken(request)
        
        if (token != null) {
            logger.info("JWT Filter: Token found, validating...")
            try {
                val username = jwtUtils.getUsernameFromToken(token)
                
                if (SecurityContextHolder.getContext().authentication == null) {
                    val userDetails = userDetailsService.loadUserByUsername(username)
                    
                    if (jwtUtils.validateToken(token, userDetails)) {
                        val authentication = UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.authorities
                        )
                        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = authentication
                        logger.info("JWT Filter: Authentication set for user: $username")
                    }
                }
            } catch (e: Exception) {
                logger.error("JWT Filter: Cannot set user authentication: ${e.message}")
            }
        } else {
            logger.info("JWT Filter: No token found, proceeding without authentication")
        }
        
        filterChain.doFilter(request, response)
    }
    
    private fun extractToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }
}

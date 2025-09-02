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

@Component
class JwtAuthenticationFilter(
    private val jwtUtils: JwtUtils,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {
    
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = extractToken(request)
        
        if (token != null) {
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
                    }
                }
            } catch (e: Exception) {
                logger.error("Cannot set user authentication: ${e.message}")
            }
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

package com.vladnekrasov.user_service.filter

import com.vladnekrasov.user_service.util.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.servletPath
        
        // Skip JWT validation for public endpoints
        if (path.startsWith("/auth/") || 
            path.startsWith("/swagger-ui/") || 
            path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response)
            return
        }
        
        val authHeader = request.getHeader("Authorization")
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            
            try {
                if (jwtUtil.validateToken(token)) {
                    val email = jwtUtil.extractEmail(token)
                    val authentication = UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        emptyList()
                    )
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (e: Exception) {
                logger.debug("JWT validation failed: ${e.message}")
            }
        }
        
        filterChain.doFilter(request, response)
    }
}

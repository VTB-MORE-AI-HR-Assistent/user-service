package com.vladnekrasov.userservice.security

import com.vladnekrasov.userservice.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    private val user: User
) : UserDetails {
    
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_USER"))
    }
    
    override fun getPassword(): String = user.password
    
    override fun getUsername(): String = user.email
    
    override fun isAccountNonExpired(): Boolean = true
    
    override fun isAccountNonLocked(): Boolean = true
    
    override fun isCredentialsNonExpired(): Boolean = true
    
    override fun isEnabled(): Boolean = true
    
    fun getId(): Long = user.id
    
    fun getUser(): User = user
}

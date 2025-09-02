package com.vladnekrasov.userservice.controller

import com.vladnekrasov.userservice.dto.*
import com.vladnekrasov.userservice.security.CustomUserDetails
import com.vladnekrasov.userservice.service.UserService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin
class UserController(
    private val userService: UserService
) {
    
    @GetMapping
    fun getAllUsers(pageable: Pageable): ResponseEntity<Page<UserDto>> {
        return ResponseEntity.ok(userService.findAll(pageable))
    }
    
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.findById(id))
    }
    
    @GetMapping("/me")
    fun getCurrentUser(@AuthenticationPrincipal userDetails: CustomUserDetails): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.findById(userDetails.getId()))
    }
    
    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateUserRequest
    ): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.update(id, request))
    }
    
    @PutMapping("/me/password")
    fun changeMyPassword(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @Valid @RequestBody request: ChangePasswordRequest
    ): ResponseEntity<Void> {
        userService.changePassword(userDetails.username, request)
        return ResponseEntity.noContent().build()
    }
    
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.delete(id)
        return ResponseEntity.noContent().build()
    }
}

package com.vladnekrasov.userservice.controller

import com.vladnekrasov.userservice.dto.*
import com.vladnekrasov.userservice.security.CustomUserDetails
import com.vladnekrasov.userservice.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "User Management", description = "User profile and management endpoints")
@SecurityRequirement(name = "bearerAuth")
class UserController(
    private val userService: UserService
) {
    
    @GetMapping
    @Operation(
        summary = "Get all users",
        description = "Retrieve a paginated list of all users. Requires authentication."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved users list",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
        ]
    )
    fun getAllUsers(
        @Parameter(description = "Pagination parameters (page, size, sort)") pageable: Pageable
    ): ResponseEntity<Page<UserDto>> {
        return ResponseEntity.ok(userService.findAll(pageable))
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Get user by ID",
        description = "Retrieve a specific user by their ID"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "User found",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserDto::class))]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
            ApiResponse(responseCode = "404", description = "User not found")
        ]
    )
    fun getUserById(
        @Parameter(description = "User ID", required = true) @PathVariable id: Long
    ): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.findById(id))
    }
    
    @GetMapping("/me")
    @Operation(
        summary = "Get current user profile",
        description = "Retrieve the profile of the currently authenticated user"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved user profile",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserDto::class))]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
        ]
    )
    fun getCurrentUser(
        @Parameter(hidden = true) @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.findById(userDetails.getId()))
    }
    
    @PutMapping("/{id}")
    @Operation(
        summary = "Update user",
        description = "Update user information (first name, last name)"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "User successfully updated",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserDto::class))]
            ),
            ApiResponse(responseCode = "400", description = "Invalid request data"),
            ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
            ApiResponse(responseCode = "404", description = "User not found")
        ]
    )
    fun updateUser(
        @Parameter(description = "User ID", required = true) @PathVariable id: Long,
        @Valid @RequestBody request: UpdateUserRequest
    ): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.update(id, request))
    }
    
    @PutMapping("/me/password")
    @Operation(
        summary = "Change current user's password",
        description = "Change the password for the currently authenticated user"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Password successfully changed"),
            ApiResponse(responseCode = "400", description = "Invalid current password or validation error"),
            ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
        ]
    )
    fun changeMyPassword(
        @Parameter(hidden = true) @AuthenticationPrincipal userDetails: CustomUserDetails,
        @Valid @RequestBody request: ChangePasswordRequest
    ): ResponseEntity<Void> {
        userService.changePassword(userDetails.username, request)
        return ResponseEntity.noContent().build()
    }
    
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete user",
        description = "Permanently delete a user account"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "User successfully deleted"),
            ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
            ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            ApiResponse(responseCode = "404", description = "User not found")
        ]
    )
    fun deleteUser(
        @Parameter(description = "User ID", required = true) @PathVariable id: Long
    ): ResponseEntity<Void> {
        userService.delete(id)
        return ResponseEntity.noContent().build()
    }
}

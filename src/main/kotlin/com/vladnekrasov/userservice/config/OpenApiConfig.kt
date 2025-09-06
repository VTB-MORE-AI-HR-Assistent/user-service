package com.vladnekrasov.userservice.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    
    @Bean
    fun customOpenAPI(): OpenAPI {
        val securitySchemeName = "bearerAuth"
        
        return OpenAPI()
            .info(
                Info()
                    .title("User Service API")
                    .version("1.0.0")
                    .description("""
                        User Service API for authentication and user management.
                        
                        ## Features
                        - JWT-based authentication
                        - User registration and login
                        - User profile management
                        - Password change functionality
                        - Token refresh mechanism
                        
                        ## Authentication
                        Most endpoints require JWT authentication. Use the `/api/v1/auth/login` endpoint to obtain a token.
                    """.trimIndent())
                    .contact(
                        Contact()
                            .name("API Support")
                            .email("support@aihr.com")
                    )
                    .license(
                        License()
                            .name("Apache 2.0")
                            .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                    )
            )
            .servers(listOf(
                Server()
                    .url("http://localhost:8082")
                    .description("Local development server"),
                Server()
                    .url("https://api.aihr.com")
                    .description("Production server")
            ))
            .addSecurityItem(SecurityRequirement().addList(securitySchemeName))
            .components(
                Components()
                    .addSecuritySchemes(
                        securitySchemeName,
                        SecurityScheme()
                            .name(securitySchemeName)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("JWT token for API authorization")
                    )
            )
    }
}
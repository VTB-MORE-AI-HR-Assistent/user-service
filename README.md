# User Service

A robust microservice for user management and authentication built with Spring Boot, Kotlin, and PostgreSQL. This service provides comprehensive user registration, authentication, and profile management capabilities with JWT-based security.

## 🚀 Features

- **User Management**: Complete CRUD operations for user profiles
- **Authentication**: JWT-based authentication with access and refresh tokens
- **Security**: Spring Security with password encryption and validation
- **Database**: PostgreSQL with Flyway migrations
- **API Documentation**: OpenAPI 3.0 with Swagger UI
- **Docker Support**: Containerized deployment
- **CI/CD**: Automated testing and deployment pipeline

## 🛠 Tech Stack

- **Language**: Kotlin 1.9.25
- **Framework**: Spring Boot 3.5.5
- **Database**: PostgreSQL 15
- **Security**: Spring Security + JWT
- **Migration**: Flyway
- **Documentation**: OpenAPI 3.0 + Swagger UI
- **Build Tool**: Gradle
- **Container**: Docker
- **CI/CD**: GitHub Actions

## 📋 Prerequisites

- Java 21 (JDK)
- Gradle 8.5+
- PostgreSQL 15+
- Docker (optional)

## 🚀 Quick Start

### Local Development

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd user-service
   ```

2. **Set up PostgreSQL**

   ```bash
   # Create database
   createdb hr_system

   # Or using Docker
   docker run -d \
     --name postgres \
     -e POSTGRES_DB=hr_system \
     -e POSTGRES_USER=postgres \
     -e POSTGRES_PASSWORD=postgres \
     -p 5432:5432 \
     postgres:15-alpine
   ```

3. **Configure environment variables**

   ```bash
   export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/hr_system
   export DB_USERNAME=postgres
   export DB_PASSWORD=postgres
   export JWT_SECRET=your-secret-key-here
   ```

4. **Run the application**

   ```bash
   ./gradlew bootRun
   ```

5. **Access the application**
   - API: http://localhost:8081
   - Swagger UI: http://localhost:8081/swagger-ui.html

### Docker Deployment

1. **Build and run with Docker**
   ```bash
   docker build -t user-service .
   docker run -d \
     --name user-service \
     -p 8081:8081 \
     -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/hr_system \
     -e DB_USERNAME=postgres \
     -e DB_PASSWORD=postgres \
     -e JWT_SECRET=your-secret-key-here \
     user-service
   ```

## 📚 API Documentation

### Authentication Endpoints

#### Register User

```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "password123"
}
```

#### Login

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

#### Refresh Token

```http
POST /api/v1/auth/refresh
Authorization: Bearer <refresh_token>
```

### User Management Endpoints

#### Get All Users

```http
GET /api/v1/users
Authorization: Bearer <access_token>
```

#### Get Current User Profile

```http
GET /api/v1/users/me
Authorization: Bearer <access_token>
```

#### Get User by ID

```http
GET /api/v1/users/{id}
Authorization: Bearer <access_token>
```

#### Update User Profile

```http
PUT /api/v1/users/{id}
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@example.com"
}
```

#### Delete User

```http
DELETE /api/v1/users/{id}
Authorization: Bearer <access_token>
```

## 🔧 Configuration

### Environment Variables

| Variable                 | Description                   | Default                                                            |
| ------------------------ | ----------------------------- | ------------------------------------------------------------------ |
| `SPRING_DATASOURCE_URL`  | Database connection URL       | `jdbc:postgresql://postgres:5432/hr_system`                        |
| `DB_USERNAME`            | Database username             | `postgres`                                                         |
| `DB_PASSWORD`            | Database password             | `postgres`                                                         |
| `JWT_SECRET`             | JWT signing secret            | `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970` |
| `JWT_EXPIRATION`         | Access token expiration (ms)  | `86400000` (24h)                                                   |
| `JWT_REFRESH_EXPIRATION` | Refresh token expiration (ms) | `604800000` (7d)                                                   |
| `SPRING_PROFILES_ACTIVE` | Active Spring profile         | `dev`                                                              |
| `SERVER_PORT`            | Application port              | `8081`                                                             |

### Database Schema

The service uses Flyway for database migrations:

- **Schema**: `users`
- **Tables**:
  - `users.users` - User profiles
  - `users.tokens` - JWT refresh tokens

## 🧪 Testing

Run tests with Gradle:

```bash
./gradlew test
```

The test suite includes:

- Unit tests for services and controllers
- Integration tests with embedded PostgreSQL
- Security tests for authentication flows

## 🚀 Deployment

### CI/CD Pipeline

The project includes a GitHub Actions workflow that:

1. Runs tests on PostgreSQL
2. Builds the application
3. Creates Docker image
4. Deploys to server (on push to `main` or `zhura` branches)

### Manual Deployment

1. **Build the application**

   ```bash
   ./gradlew bootJar
   ```

2. **Create Docker image**

   ```bash
   docker build -t user-service .
   ```

3. **Deploy to server**
   ```bash
   docker run -d \
     --name user-service \
     --restart unless-stopped \
     -p 8081:8081 \
     -e SPRING_PROFILES_ACTIVE=production \
     -e DB_USERNAME=<db-username> \
     -e DB_PASSWORD=<db-password> \
     -e SPRING_DATASOURCE_URL=<db-url> \
     -e JWT_SECRET=<jwt-secret> \
     user-service
   ```

## 📁 Project Structure

```
src/main/kotlin/com/vladnekrasov/user_service/
├── config/                 # Configuration classes
├── controller/            # REST API controllers
├── dto/                   # Data Transfer Objects
├── exception/             # Custom exceptions
├── mapper/                # Object mappers
├── model/                 # Domain entities
├── repository/            # Data access layer
├── security/              # Security configuration
└── service/               # Business logic
    └── impl/              # Service implementations
```

## 🔒 Security

- **Password Encryption**: BCrypt hashing
- **JWT Authentication**: Access and refresh tokens
- **Input Validation**: Comprehensive request validation
- **CORS Configuration**: Configurable cross-origin settings
- **Rate Limiting**: Built-in protection against abuse

## 📊 Monitoring

- **Health Checks**: `/actuator/health`
- **Metrics**: Prometheus-compatible metrics
- **Logging**: Structured JSON logging
- **Tracing**: Distributed tracing support

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:

- Create an issue in the repository
- Check the API documentation at `/swagger-ui.html`
- Review the logs for debugging information

---

**Built with ❤️ using Spring Boot and Kotlin**

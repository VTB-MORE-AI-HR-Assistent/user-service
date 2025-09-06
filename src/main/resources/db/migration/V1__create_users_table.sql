CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL
);

CREATE INDEX idx_users_email ON users(email);

-- Создаем начального пользователя
INSERT INTO users (email, password, first_name, last_name)
VALUES (
    'admin@hr.com',
    '$2a$10$5xQpMfGVPnwqZ8c5.8RXGuA1zLbXVe9cI8PmyVgQcXPcGWGzYL.5G', -- password: admin123
    'Admin',
    'User'
) ON CONFLICT (email) DO NOTHING;

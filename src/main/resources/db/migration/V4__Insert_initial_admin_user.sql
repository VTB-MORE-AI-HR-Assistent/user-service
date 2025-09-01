-- Insert initial admin user with BCrypt hashed password
-- Default password: admin123
INSERT INTO users.users (firstname, lastname, email, password, role)
VALUES (
    'Admin',
    'User',
    'admin@aihr.com',
    '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', -- BCrypt hash of 'admin123'
    'ADMIN'
) ON CONFLICT (email) DO NOTHING;
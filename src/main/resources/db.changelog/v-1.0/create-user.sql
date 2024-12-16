CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       user_name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       role VARCHAR(50) NOT NULL
);
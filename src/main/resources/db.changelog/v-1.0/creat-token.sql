CREATE TABLE refresh_token (
                               id SERIAL PRIMARY KEY,
                               user_id BIGINT NOT NULL,
                               token VARCHAR(255) NOT NULL,
                               CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
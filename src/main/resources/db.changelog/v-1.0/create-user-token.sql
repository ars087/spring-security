CREATE TABLE user_one (
                          user_name VARCHAR(255) PRIMARY KEY,
                          password VARCHAR(255) NOT NULL,
                          is_account_non_locked BOOLEAN DEFAULT TRUE,
                          failed_attempts INT DEFAULT 0,
                          role VARCHAR(50) NOT NULL
);


CREATE TABLE token (
                       id SERIAL NOT NULL    PRIMARY KEY,
                       user_name VARCHAR(255),
                       token VARCHAR(255) NOT NULL,
                       FOREIGN KEY (user_name) REFERENCES user_one(user_name)
);
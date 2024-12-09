CREATE TABLE log_entry
(
    id         SERIAL       PRIMARY KEY,
    message    VARCHAR(255) NOT NULL,
    message_info    VARCHAR(255) NOT NULL,
    message_info_second varchar(255) not null,
    timestamp  VARCHAR(255) NOT NULL

);
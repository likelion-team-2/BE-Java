-- +migrate Up
CREATE TABLE users (
                       user_id VARCHAR(255) PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       nickname VARCHAR(255),
                       region_country VARCHAR(255),
--                        role VARCHAR(255) CHECK (role IN ('admin', 'users')) DEFAULT NULL,
                       created_at TIMESTAMP(3) DEFAULT NULL,
                       updated_at TIMESTAMP(3) DEFAULT NULL,
                       token VARCHAR(255),
                       expired_at TIMESTAMP(3) DEFAULT NULL
);

-- +migrate Down
DROP TABLE IF EXISTS users;

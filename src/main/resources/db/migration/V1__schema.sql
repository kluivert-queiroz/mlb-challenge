DROP TABLE IF EXISTS user;

CREATE TABLE user (
    id   BIGINT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    email  VARCHAR(255) NOT NULL UNIQUE,
    cpf  VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE FULLTEXT INDEX user_name_idx ON user(name);

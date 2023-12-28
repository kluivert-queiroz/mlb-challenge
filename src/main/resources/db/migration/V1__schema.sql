DROP TABLE IF EXISTS user;

CREATE TABLE user (
    id   BIGINT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    email  VARCHAR(255) NOT NULL UNIQUE,
    cpf  VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL
);
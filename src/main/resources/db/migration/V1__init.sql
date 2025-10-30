CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    login    VARCHAR(64) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL
);

CREATE TABLE locations
(
    id        BIGSERIAL PRIMARY KEY,
    name      varchar(64) UNIQUE NOT NULL,
    user_id   BIGINT REFERENCES users (id),
    latitude  DECIMAL             NOT NULL,
    longitude DECIMAL             NOT NULL
);

CREATE TABLE sessions
(
    id        UUID PRIMARY KEY,
    user_id   BIGINT REFERENCES users (id),
    expiresAt TIMESTAMP
);
CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    login    VARCHAR(64) UNIQUE NOT NULL,
    password VARCHAR(60)       NOT NULL
);

CREATE TABLE locations
(
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(64)    NOT NULL,
    user_id   BIGINT REFERENCES users (id),
    latitude  DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    CONSTRAINT unique_lat_lon UNIQUE (latitude, longitude)
);

CREATE TABLE sessions
(
    id        UUID PRIMARY KEY,
    user_id   BIGINT REFERENCES users (id),
    expiresAt TIMESTAMP
);
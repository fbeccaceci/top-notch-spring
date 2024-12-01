CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(32) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'DISABLED',
    email_verified_at TIMESTAMP NULL,
    locale VARCHAR(255) NOT NULL DEFAULT 'en'
);

CREATE TABLE user_otps
(
    id       UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id  UUID         NOT NULL REFERENCES users (id),
    otp_code VARCHAR(255) NOT NULL UNIQUE,
    sent_at  TIMESTAMP    NOT NULL,
    status   VARCHAR(255) NOT NULL,
    type     VARCHAR(255) NOT NULL
);

CREATE TABLE refresh_tokens
(
    id      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID         NOT NULL REFERENCES users (id),
    token   VARCHAR(255) NOT NULL UNIQUE,
    status  VARCHAR(255) NOT NULL
);
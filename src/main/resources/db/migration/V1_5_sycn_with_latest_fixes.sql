ALTER TABLE user_otps
    ADD COLUMN expires_at TIMESTAMP NOT NULL default now() + interval '24 hour';

CREATE TABLE roles
(
    id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE users_roles
(
    user_id UUID REFERENCES users (id),

    role_id UUID REFERENCES roles (id),

    CONSTRAINT users_roles_pk PRIMARY KEY (user_id, role_id)
);

CREATE TABLE privileges
(
    id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE roles_privileges
(
    role_id UUID REFERENCES roles (id),

    privilege_id UUID REFERENCES privileges (id),

    CONSTRAINT roles_privileges_pk PRIMARY KEY (role_id, privilege_id)
);

ALTER TABLE users
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(255),
    ADD COLUMN last_updated_at TIMESTAMP DEFAULT NULL,
    ADD COLUMN last_updated_by VARCHAR(255) DEFAULT NULL;

UPDATE users
SET created_at = CURRENT_TIMESTAMP,
    created_by = 'system';

ALTER TABLE users
    ALTER COLUMN created_at SET NOT NULL,
    ALTER COLUMN created_by SET NOT NULL;


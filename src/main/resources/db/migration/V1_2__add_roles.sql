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


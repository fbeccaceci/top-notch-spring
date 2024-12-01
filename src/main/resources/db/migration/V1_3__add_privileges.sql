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


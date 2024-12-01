ALTER TABLE user_otps
    ADD COLUMN expires_at TIMESTAMP NOT NULL default now() + interval '24 hour';
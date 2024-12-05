-- Step 1: Add new columns to the books table
ALTER TABLE books
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(255),
    ADD COLUMN last_updated_at TIMESTAMP DEFAULT NULL,
    ADD COLUMN last_updated_by VARCHAR(255) DEFAULT NULL;

-- Step 2: Populate new columns with default values for existing rows
UPDATE books
SET created_at = CURRENT_TIMESTAMP,
    created_by = 'system';

-- Step 3: Add NOT NULL constraints for created_at and created_by
ALTER TABLE books
    ALTER COLUMN created_at SET NOT NULL,
    ALTER COLUMN created_by SET NOT NULL;

ALTER TABLE users
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(255),
    ADD COLUMN last_updated_at TIMESTAMP DEFAULT NULL,
    ADD COLUMN last_updated_by VARCHAR(255) DEFAULT NULL;

-- Step 2: Populate new columns with default values for existing rows
UPDATE users
SET created_at = CURRENT_TIMESTAMP,
    created_by = 'system';

-- Step 3: Add NOT NULL constraints for created_at and created_by
ALTER TABLE users
    ALTER COLUMN created_at SET NOT NULL,
    ALTER COLUMN created_by SET NOT NULL;
-- Liquibase formatted SQL
-- Changeset author:DenysDovgan

CREATE TABLE issues (
                        id SERIAL PRIMARY KEY,
                        created_by INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                        title VARCHAR(255) NOT NULL,
                        description TEXT NOT NULL,
                        deleted BOOLEAN NOT NULL DEFAULT FALSE NOT NULL,
                        status VARCHAR(20) NOT NULL CHECK (status IN ('DRAFT', 'OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')),
                        created_at TIMESTAMP DEFAULT NOW(),
                        updated_at TIMESTAMP DEFAULT NOW()
);

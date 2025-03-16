-- Liquibase formatted SQL
-- Changeset author:DenysDovgan

CREATE TABLE media (
                       id SERIAL PRIMARY KEY,
                       issue_id INT NOT NULL REFERENCES issues(id) ON DELETE CASCADE,
                       url TEXT NOT NULL,
                       type VARCHAR(50) NOT NULL, -- Example: image/png, video/mp4
                       size BIGINT NOT NULL, -- File size in bytes
                       uploaded_at TIMESTAMP DEFAULT NOW()
);
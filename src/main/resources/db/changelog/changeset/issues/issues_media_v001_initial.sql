-- Liquibase formatted SQL
-- Changeset author:DenysDovgan

CREATE TABLE issue_media (
                             id SERIAL PRIMARY KEY,
                             issue_id INT NOT NULL REFERENCES issue(id) ON DELETE CASCADE,
                             media_url TEXT NOT NULL
);

-- Liquibase formatted SQL
-- Changeset author:DenysDovgan

CREATE TABLE issues_media (
                             id SERIAL PRIMARY KEY,
                             issue_id INT NOT NULL REFERENCES issues(id) ON DELETE CASCADE,
                             media_url TEXT NOT NULL
);

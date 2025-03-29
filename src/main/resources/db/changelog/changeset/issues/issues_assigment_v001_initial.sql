-- Liquibase formatted SQL
-- Changeset author:DenysDovgan

CREATE TABLE issues_assignment (
                                   id SERIAL PRIMARY KEY,
                                   issue_id INT NOT NULL REFERENCES issues(id) ON DELETE CASCADE,
                                   technician_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                   assigned_by INT REFERENCES users(id) ON DELETE SET NULL,
                                   assigned_at TIMESTAMP DEFAULT NOW()
);

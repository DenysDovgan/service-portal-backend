-- Liquibase formatted SQL
-- Changeset author:DenysDovgan

CREATE TABLE issue_assignment (
                                  id SERIAL PRIMARY KEY,
                                  issue_id INT NOT NULL REFERENCES issue(id) ON DELETE CASCADE,
                                  technician_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                  assigned_by INT NOT NULL REFERENCES users(id) ON DELETE SET NULL,
                                  assigned_at TIMESTAMP DEFAULT NOW()
);

-- Liquibase formatted SQL
-- Changeset author:DenysDovgan

CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_issues_status ON issue(status);
CREATE INDEX idx_issues_created_at ON issue(created_at);

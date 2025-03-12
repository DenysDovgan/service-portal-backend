-- Liquibase formatted SQL
-- Changeset author:DenysDovgan

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password_hash TEXT NOT NULL,
                       phone_number VARCHAR(20) NOT NULL,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       city VARCHAR(100) NOT NULL,
                       country VARCHAR(100) NOT NULL,
                       company_name VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL CHECK (role IN ('CLIENT', 'SERVICE_TECHNICIAN', 'SERVICE_MANAGER', 'ADMIN')),
                       created_at TIMESTAMP DEFAULT NOW(),
                       updated_at TIMESTAMP DEFAULT NOW()
);

-- PostgreSQL Schema generated from Java Entities

-- Create ENUM type for RoleName
CREATE TYPE role_name AS ENUM ('ADMIN', 'MANAGER', 'STAFF', 'CUSTOMER');

-- Create ROLE table
CREATE TABLE ROLE (
    role_id SERIAL PRIMARY KEY,
    role_name role_name NOT NULL
);

-- Create ACCOUNT table
CREATE TABLE ACCOUNT (
    email VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255),
    status BOOLEAN,
    google_id VARCHAR(255) UNIQUE,
    verified BOOLEAN DEFAULT FALSE,
    refresh_token TEXT,
    refresh_token_expiry TIMESTAMP,
    role_id INTEGER REFERENCES ROLE(role_id)
);

-- Create CUSTOMER table
CREATE TABLE CUSTOMER (
    email VARCHAR(255) PRIMARY KEY REFERENCES ACCOUNT(email),
    username VARCHAR(255),
    last_name VARCHAR(255),
    first_name VARCHAR(255),
    gender BOOLEAN,
    address TEXT,
    date_of_birth VARCHAR(255),
    phone_number VARCHAR(255),
    avatar TEXT
);

-- Create EMPLOYEE table
CREATE TABLE EMPLOYEE (
    email VARCHAR(255) PRIMARY KEY REFERENCES ACCOUNT(email),
    employee_name VARCHAR(255),
    last_name VARCHAR(255),
    first_name VARCHAR(255),
    gender BOOLEAN,
    address TEXT,
    date_of_birth VARCHAR(255),
    phone_number VARCHAR(255),
    avatar TEXT,
    social_insurance_number VARCHAR(255)
);

-- Create indexes
CREATE INDEX idx_account_role ON ACCOUNT(role_id);

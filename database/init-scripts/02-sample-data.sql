-- Copyright (c) 2023 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    last_online TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
);
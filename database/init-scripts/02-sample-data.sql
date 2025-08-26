-- Copyright (c) 2023 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.

CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
	email VARCHAR(100) UNIQUE,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);
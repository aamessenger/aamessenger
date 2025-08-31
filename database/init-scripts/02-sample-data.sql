
-- Copyright (c) 2025 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.

CREATE TABLE IF NOT EXISTS users (
    guest BOOLEAN DEFAULT FALSE,
    username VARCHAR(50) UNIQUE NOT NULL,
	email VARCHAR(100) UNIQUE,
    password_hash VARCHAR(255),
	CONSTRAINT email_required_if_not_guest CHECK (guest = TRUE OR email IS NOT NULL),
	CONSTRAINT password_required_if_not_guest CHECK (guest = TRUE OR password_hash IS NOT NULL)
);
    
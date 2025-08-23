# Copyright (c) 2023 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.

import bcrypt

def hash_password(password: str) -> str:
    """Hash a password for storing."""
    salt = bcrypt.gensalt()
    hashed = bcrypt.hashpw(password.encode('utf-8'), salt)
    return hashed.decode('utf-8')


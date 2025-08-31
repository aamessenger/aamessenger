# Copyright (c) 2025 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.

import hashlib
import psycopg2
import random
import string
import secrets
import re
import sys
import os
sys.path.append(os.path.join(os.path.dirname(__file__), '..', 'scripts'))
import password_security_checker as psc

connection = psycopg2.connect(dbname="postgres", user="postgres", password="databasepassword", host="localhost", port=5432)
cursor = connection.cursor()

def create_user_with_email(username: str, email: str, password:str):
    cursor.execute((
        """SELECT * FROM users WHERE username = %s OR email = %s"""
    ), (username, email))
    if cursor.fetchone():
        raise ValueError("Username or email already exists")
    
    # Basic email format validation
    if not re.match(r'^[^@]+@[^@]+\.[^@]+$', email):
        raise ValueError("Invalid email format")
    
    # Check password security
    psc.password_check(password)

    password_hash = hash_password(password)

    cursor.execute((
        """INSERT INTO users (guest, username, email, password_hash) VALUES (FALSE, %s, %s, %s)"""
    ), (username, email, password_hash))
    pass

def create_user_guest():
    while True:
        username = ''.join(random.choice(string.ascii_lowercase + string.digits) for _ in range(8))
        cursor.execute((
            """SELECT * FROM users WHERE username = %s"""
        ), (username,))
        if not cursor.fetchone():
            break
    
    cursor.execute((
        """INSERT INTO users (username, guest) VALUES (%s, TRUE)"""
    ), (username,))
    return username
    

def hash_password(password: str) -> str:
    salt = secrets.token_hex(16)
    hash_obj = hashlib.sha256()
    hash_obj.update(salt.encode('utf-8') + password.encode('utf-8'))
    return salt + hash_obj.hexdigest()

def verify_password(password: str, hashed: str) -> bool:
    salt = hashed[:32]
    stored_hash = hashed[32:]
    hash_obj = hashlib.sha256()
    hash_obj.update(salt.encode('utf-8') + password.encode('utf-8'))
    if hash_obj.hexdigest() != stored_hash:
        assert False, "Password does not match"
    return True

def verify_user(username_or_email: str, password: str) -> bool:
    cursor.execute((
        """SELECT password_hash FROM users WHERE username = %s AND guest = FALSE"""
    ), (username_or_email,))
    result = cursor.fetchone()
    if not result:
        cursor.execute((
            """SELECT password_hash FROM users WHERE email = %s AND guest = FALSE"""
        ), (username_or_email,))
        result = cursor.fetchone()
    if not result:
        assert False, "User not found"
    stored_hash = result[0]
    return verify_password(password, stored_hash)

'''def main():
    print(verify_user("aaaa@aa.com", "Hello_World_1"))

if __name__ == "__main__":
    main()'''

connection.commit()
cursor.close()
connection.close()
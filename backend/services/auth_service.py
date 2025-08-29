# Copyright (c) 2025 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.

import bcrypt
import psycopg2
import random
import string

connection = psycopg2.connect(dbname="postgres", user="postgres", password="databasepassword", host="localhost", port=5432)
cursor = connection.cursor()

def create_user_with_email(username: str, email: str, password:str):
    check_unique = cursor.execute((
        """SELECT * FROM users WHERE username = %s OR email = %s"""
    ), (username, email))
    if cursor.fetchone():
        raise ValueError("Username or email already exists")
    
    password_hash = hash_password(password)

    cursor.execute((
        """INSERT INTO users (guest, username, email, password_hash) VALUES (FALSE, %s, %s, %s)"""
    ), (username, email, password_hash))
    pass

def create_user_guest():
    check_unique = False
    while not check_unique:
        username = ''.join(random.choice(string.ascii_lowercase + string.digits) for _ in range(8))
        cursor.execute((
            """SELECT * FROM users WHERE username = %s"""
        ), (username,))
        if not cursor.fetchone():
            check_unique = True
    
    cursor.execute((
        """INSERT INTO users (username, guest) VALUES (%s, TRUE)"""
    ), (username,))
    return username
    

def hash_password(password: str) -> str:
    salt = bcrypt.gensalt()
    hashed = bcrypt.hashpw(password.encode('utf-8'), salt)
    return hashed.decode('utf-8')

def verify_password(password: str, hashed: str) -> bool:
    return bcrypt.checkpw(password.encode('utf-8'), hashed.encode('utf-8'))

def main():
    create_user_with_email("aa", "NULL", "NULL")

if __name__ == "__main__":
    main()
connection.commit()
cursor.close()
connection.close()  
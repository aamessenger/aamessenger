# Copyright (c) 2023 Andrejs Grišins, Anastasia Petrova. Unauthorized use prohibited.

import bcrypt
import psycopg2

connection = psycopg2.connect(dbname="postgres", user="postgres", password="databasepassword", host="localhost", port=5432)
cursor = connection.cursor()

def create_user_with_email(username: str, email: str, password:str) -> str:
    check_unique = cursor.execute((
        """SELECT * FROM users WHERE username = %s OR email = %s"""
    ), (username, email))
    if cursor.fetchone():
        raise ValueError("Username or email already exists")
    
    password_hash = hash_password(password)

    cursor.execute((
        """INSERT INTO users (username, email, password_hash) VALUES (%s, %s, %s)"""
    ), (username, email, password_hash))
    return("success")
    pass

def hash_password(password: str) -> str:
    salt = bcrypt.gensalt()
    hashed = bcrypt.hashpw(password.encode('utf-8'), salt)
    return hashed.decode('utf-8')

def verify_password(password: str, hashed: str) -> bool:
    return bcrypt.checkpw(password.encode('utf-8'), hashed.encode('utf-8'))

def main():
    create_user_with_email("seabuckthorn", "keklolazaza92@gmail.com", "keklolazaza92")

if __name__ == "__main__":
    main()
connection.commit()
cursor.close()
connection.close()
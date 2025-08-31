import hashlib
import requests

def checkLength(password):
    if len(password) < 12:
        raise ValueError("Password is too short (minimum 12 characters).")
    pass

def checkAllCharTypes(password):
    if any(ord(c) > 127 for c in password):
        raise AssertionError("Password contains not acceptable characters.")
    if not any(char.islower() for char in password):
        raise ValueError("Password must include at least one lowercase letter.")
    if not any(char.isupper() for char in password):
        raise ValueError("Password must include at least one uppercase letter.")
    if not any(char.isdigit() for char in password):
        raise ValueError("Password must include at least one digit.")
    symbols = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/~`"
    if not any(char in symbols for char in password):
        raise ValueError("Password must include at least one special character.")
    pass

def pwned(password):
    sha1 = hashlib.sha1(password.encode()).hexdigest().upper()
    prefix, suffix = sha1[:5], sha1[5:]
    res = requests.get(f"https://api.pwnedpasswords.com/range/{prefix}")
    if res.status_code != 200:
        raise RuntimeError(f"Error fetching data from API: {res.status_code}, {res.text}")
    hashes = (line.split(':') for line in res.text.splitlines())
    return any(h[0] == suffix for h in hashes)


# Main function to check password security
def password_check(password):
    checkLength(password)
    checkAllCharTypes(password)
    if pwned(password):
        raise ValueError("Your password has been leaked. Please, choose another one.")
    pass

'''def main():
    password = str(input("Enter a password: "))
    password_check(password)

if __name__=="__main__":
    main()'''

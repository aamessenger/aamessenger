import math
import hashlib
import requests

def checkLength(password):
    if len(password) < 12:
        return False
    return True

def checkAllCharTypes(password):
    if any(ord(c) > 127 for c in password):
        raise AssertionError("Password contains not acceptable characters.")
    lower, upper, digit, symbol = False, False, False, False
    charset_size = 0
    if any(char.islower() for char in password):
        charset_size += 26
        lower = True
    if any(char.isupper() for char in password):
        charset_size += 26
        upper = True
    if any(char.isdigit() for char in password):
        charset_size += 10
        digit = True
    symbols = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/~`"
    if any(char in symbols for char in password):
        charset_size += len(symbols)
        symbol = True
    entropy = len(password) * math.log2(charset_size)
    return lower, upper, digit, symbol, entropy

def pwned(password):
    sha1 = hashlib.sha1(password.encode()).hexdigest().upper()
    prefix, suffix = sha1[:5], sha1[5:]
    res = requests.get(f"https://api.pwnedpasswords.com/range/{prefix}")
    if res.status_code != 200:
        raise RuntimeError(f"Error fetching data from API: {res.status_code}, {res.text}")
    hashes = (line.split(':') for line in res.text.splitlines())
    return any(h[0] == suffix for h in hashes)

def output(password, isLengthGood, lower, upper, digit, symbol, entropy, hasBeenPwned):
    issues = 1
    if (isLengthGood and lower and upper and digit and symbol):
        print("Strength: Strong")
        issues = 0
    else:
        print("Strength: Weak")
    print("Entropy: ", entropy)
    if issues > 0:
        print("Issues:")
        if not isLengthGood:
            print(" - Password is too short (minimum 12 characters).")
        if not lower:
            print(" - Password must contain at least one lowercase letter.")
        if not upper:
            print(" - Password must contain at least one uppercase letter.")
        if not digit:
            print(" - Password must contain at least one digit.")
        if not symbol:
            print(" - Password must contain at least one special character.")
    else:
        print("Issues: None")
    if (hasBeenPwned):
        print("Pwned: Yes")
    else:
        print("Pwned: No")
        


def main():
    password = str(input("Enter a password: "))
    isLengthGood = checkLength(password)
    lower, upper, digit, symbol, entropy = checkAllCharTypes(password)
    hasBeenPwned = pwned(password)
    output(password, isLengthGood, lower, upper, digit, symbol, entropy, hasBeenPwned)

if __name__=="__main__":
    main()

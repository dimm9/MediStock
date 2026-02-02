import bcrypt

def create_hash():
    password = input("Enter password to hash: ")
    # bcrypt.hashpw requires bytes, so we encode the password
    # bcrypt.gensalt() generates a random salt
    hashed = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt())
    
    # Decode back to string for easy copying
    print(f"BCrypt Hash: {hashed.decode('utf-8')}")

if __name__ == "__main__":
    create_hash()
import psycopg2
from psycopg2.extras import execute_values
import bcrypt

# Prompt for database credentials
DB_CONFIG = {
    "dbname": "neondb",
    "user": input("Enter your database username: "),
    "password": input("Enter your database password: "),
    "host": "ep-lively-rice-ag7y48kz.c-2.eu-central-1.aws.neon.tech",
    "port": 5432
}

# Sample data for each table
hospitals = [
    ("City General Hospital", "123 Main St", 100000.00),
    ("Elmwood Medical Center", "456 Elm St", 200000.00),
    ("Oak Valley Hospital", "789 Oak St", 150000.00),
    ("Pinecrest Clinic", "321 Pine St", 175000.00),
    ("Maplewood Health Center", "654 Maple St", 125000.00)
]

stocks = [
    (1, "Basic Medical Supplies", "BASIC_MEDICAL_EQUIPMENT"),
    (2, "Pharmaceuticals", "MEDICINES"),
    (3, "Lab Testing Kits", "LABORATORY_EQUIPMENT"),
    (4, "Surgical Instruments", "TECHNICAL_EQUIPMENT"),
    (5, "Advanced Imaging Equipment", "SPECIALIZED_MEDICAL_EQUIPMENT")
]

products = [
    (1, "Stethoscope", "Diagnostic Tool", 50.00, 100, True, "http://example.com/stethoscope"),
    (2, "Antibiotics", "Medication", 75.00, 200, True, "http://example.com/antibiotics"),
    (3, "Blood Test Kit", "Lab Equipment", 100.00, 150, True, "http://example.com/blood_test_kit"),
    (4, "Scalpel", "Surgical Tool", 25.00, 300, True, "http://example.com/scalpel"),
    (5, "MRI Machine", "Imaging Equipment", 60000.00, 2, True, "http://example.com/mri_machine")
]

def bcrypt_hash(plain: str) -> str:
    return bcrypt.hashpw(plain.encode("utf-8"), bcrypt.gensalt()).decode("utf-8")


employees = [
    (1, "Dr. John Doe", "ADMINISTRATOR", 5000.00, "johndoe", bcrypt_hash("password123"), True),
    (2, "Dr. Jane Smith", "DOCTOR", 4000.00, "janesmith", bcrypt_hash("password123"), True),
    (3, "Nurse Alice Brown", "NURSE", 3000.00, "alicebrown", bcrypt_hash("password123"), True),
    (4, "Bob White", "WAREHOUSE_WORKER", 2500.00, "bobwhite", bcrypt_hash("password123"), True),
    (5, "Dr. Charlie Black", "DOCTOR", 4500.00, "charlieblack", bcrypt_hash("password123"), True)
]

# Insert data into the database
def insert_data():
    try:
        # Connect to the database
        conn = psycopg2.connect(**DB_CONFIG)
        cursor = conn.cursor()

        cursor.execute("DELETE FROM employee")

        # Insert hospitals
        execute_values(cursor, """
            INSERT INTO hospital (name, address, funds) VALUES %s
        """, hospitals)

        # Insert stocks
        execute_values(cursor, """
            INSERT INTO stock (hospital_id, name, category) VALUES %s
        """, stocks)

        # Insert products
        execute_values(cursor, """
            INSERT INTO product (stock_id, name, type, cost, quantity, is_available, media_url) VALUES %s
        """, products)

        # Insert employees
        execute_values(cursor, """
            INSERT INTO employee (hospital_id, name, role, salary, login, password_hash, active) VALUES %s
        """, employees)

        # Commit the transaction
        conn.commit()
        print("Data inserted successfully!")

    except Exception as e:
        print(f"An error occurred: {e}")
    finally:
        # Close the connection
        if conn:
            cursor.close()
            conn.close()

if __name__ == "__main__":
    insert_data()
import psycopg2

# Prompt for database credentials
DB_CONFIG = {
    "dbname": "neondb",
    "user": input("Enter your database username: "),
    "password": input("Enter your database password: "),
    "host": "ep-lively-rice-ag7y48kz.c-2.eu-central-1.aws.neon.tech",
    "port": 5432
}

# Path to the SQL file
SQL_FILE_PATH = "database_formula.sql"

def initialize_tables():
    try:
        # Connect to the database
        conn = psycopg2.connect(**DB_CONFIG)
        cursor = conn.cursor()

        # Read the SQL file
        with open(SQL_FILE_PATH, "r") as sql_file:
            sql_commands = sql_file.read()

        # Execute the SQL commands
        cursor.execute(sql_commands)
        conn.commit()
        print("Tables initialized successfully!")

    except Exception as e:
        print(f"An error occurred: {e}")
    finally:
        # Close the connection
        if conn:
            cursor.close()
            conn.close()

if __name__ == "__main__":
    initialize_tables()
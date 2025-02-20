from flask import Flask, jsonify
import mysql.connector
import bcrypt
import random
import string
from datetime import datetime, timedelta
from flask_cors import CORS

app = Flask(__name__)

# CORS configuration to allow requests from your Vue frontend
CORS(app, origins=["http://localhost:8081"])  # Allow requests from Vue.js running on port 8081

def get_db_connection():
    return mysql.connector.connect(
        host="mysql",  # Use Docker MySQL container's name or your database host
        user="root",
        password="chatapp99",
        database="chatapp"
    )

@app.route("/stats", methods=["GET"])
def get_stats():
    connection = get_db_connection()
    cursor = connection.cursor()

    # Example query to fetch statistics
    cursor.execute("SELECT COUNT(*) FROM user")  # Assuming `user` table exists
    result = cursor.fetchone()

    cursor.close()
    connection.close()

    return jsonify({"user_count": result[0]})

# some names that we use for the generation
first_names = [
    "Alexander", "James", "Olivia", "Sophia", "Liam", "Mason", "Emma", "Ethan", "Ava", "Lucas",
    "Jackson", "Amelia", "Isabella", "Charlotte", "Zoe", "Benjamin", "William", "Daniel", "Henry",
    "Charlotte", "Chloe", "Michael", "Samuel", "Grace", "Jack", "Mia", "Harper", "Ella", "Daniel",
    "Matthew", "Scarlett", "Hannah", "Emily", "Joseph", "Owen", "Megan", "Madison", "Isla", "Leo",
    "Sophie", "Oliver", "Gabriel", "Leo", "Julian", "Eva", "Aiden", "Samantha", "Rachel", "Victoria"
]

last_names = [
    "Schrammer", "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore",
    "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia", "Martinez",
    "Roberts", "Lee", "Young", "Walker", "Hall", "Allen", "King", "Wright", "Scott", "Adams", "Baker",
    "Gonzalez", "Nelson", "Carter", "Mitchell", "Perez", "Hernandez", "Graham", "Cameron", "Collins", "Reed",
    "Cook", "Morgan", "Bell", "Murphy", "Cooper", "Foster", "Bryant", "Russell"
]


# Function to generate a random username
def generate_username():
    first_name = random.choice(first_names).lower()  # Choose a random first name and convert to lowercase
    last_name = random.choice(last_names).lower()   # Choose a random last name and convert to lowercase
    number = random.randint(10, 99)  # Random two-digit number for uniqueness
    return f"{first_name}{last_name}{number}"

# Function to generate an email address
def generate_email(username):
    return f"{username}@chatapp.com"


# Function to generate bcrypt encoded password
def generate_password():
    plain_password = "password123"
    bcrypt_hash = bcrypt.hashpw(plain_password.encode('utf-8'), bcrypt.gensalt())
    return bcrypt_hash.decode('utf-8')

def check_if_exists(cursor, username, email):
    cursor.execute("SELECT * FROM user WHERE username = %s OR email = %s", (username, email))
    return cursor.fetchone()  # Returns None if no match is found

@app.route("/generate_users", methods=["POST"])
def generate_users():
    # Number of users to insert
    num_users = 50
    connection = get_db_connection()
    cursor = connection.cursor()

    # Generate and insert users into the database
    inserted_count = 0  # Track the number of successfully inserted users
    while inserted_count < num_users:
        username = generate_username()
        email = generate_email(username)
        password = generate_password()

        # Check if the username or email already exists in the database
        if check_if_exists(cursor, username, email):
            continue  # Skip this iteration if the username or email already exists

        # Insert the new user
        insert_query = """
        INSERT INTO user (username, email, password)
        VALUES (%s, %s, %s)
        """
        cursor.execute(insert_query, (username, email, password))

        # Increment the count of inserted users
        inserted_count += 1

    connection.commit()
    cursor.close()
    connection.close()

    return jsonify({"message": f"{num_users} users have been generated and inserted into the database!"})

def generate_random_date():
       start_date = datetime(2020, 1, 1)
       end_date = datetime(2024, 12, 31)
       delta = end_date - start_date
       random_days = random.randint(0, delta.days)
       random_date = start_date + timedelta(days=random_days)
       return random_date


@app.route("/generate_friendships", methods=["POST"])
def generate_friendships():
    # Number of friendships to generate
    num_friendships = 25

    # Get a database connection
    connection = get_db_connection()
    cursor = connection.cursor()

    # Fetch all users from the database
    cursor.execute("SELECT userid FROM user")
    users = cursor.fetchall()

    # Check the current number of friendships in the database
    cursor.execute("SELECT COUNT(*) FROM friendship")
    existing_friendships = cursor.fetchone()[0]

    # Calculate the maximum possible friendships
    num_users = len(users)
    max_possible_friendships = num_users * (num_users - 1) // 2

    # If the number of existing friendships is equal to the maximum possible, return an error
    if existing_friendships >= max_possible_friendships:
        cursor.close()
        connection.close()
        return jsonify({"error": "All possible friendships are already created. Cannot create more."}), 400

    friendships_created = 0
    while friendships_created < num_friendships:
        # Select two random users
        user1 = random.choice(users)
        user2 = random.choice(users)

        # Ensure user1 and user2 are not the same
        if user1[0] != user2[0]:
            # Check if the friendship already exists (avoid duplicates like user1-user2 and user2-user1)
            cursor.execute("""
                SELECT * FROM friendship WHERE (user1_id = %s AND user2_id = %s) OR (user1_id = %s AND user2_id = %s)
            """, (user1[0], user2[0], user2[0], user1[0]))
            existing_friendship = cursor.fetchone()

            if not existing_friendship:
                # Randomly decide which user sent the request
                requester = random.choice([user1[0], user2[0]])

                # Generate a random date for `created_at`
                created_at = generate_random_date()

                # Insert the friendship into the database
                cursor.execute("""
                    INSERT INTO friendship (user1_id, user2_id, requested_by, created_at, status)
                    VALUES (%s, %s, %s, %s, %s)
                """, (user1[0], user2[0], requester, created_at, 1))  # '1' is True for status

                # Commit the transaction
                connection.commit()

                friendships_created += 1

    cursor.close()
    connection.close()

    return jsonify({"message": f"{num_friendships} friendships have been generated and inserted into the database!"})


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)

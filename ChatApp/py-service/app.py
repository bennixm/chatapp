from flask import Flask, jsonify
from flask_cors import CORS
import mysql.connector
import generators  # we take all the generators from generators.py
from getters import get_user_count  # we take the user count function from getters

app = Flask(__name__)
CORS(app, origins=["http://localhost:8081"])  # Allow requests from Vue.js frontend

@app.route("/stats", methods=["GET"])
def stats():
    # Fetch user count using the function from getters.py
    user_count = get_user_count()

    return jsonify({"user_count": user_count})

@app.route("/generate_users", methods=["POST"])
def generate_users():
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

    # Call the generator to create users
    generators.generate_users(first_names, last_names, num_users=50)

    return jsonify({"message": "50 users have been generated and inserted into the database!"})

# Route to generate friendships
@app.route("/generate_friendships", methods=["POST"])
def generate_friendships():
    # Call the generator to create friendships
    generators.generate_friendships(num_friendships=50)

    return jsonify({"message": "50 friendships have been generated and inserted into the database!"})

# Route to generate chats
@app.route("/generate_chats", methods=["POST"])
def generate_chats():
    num_chats=50
    result = generators.generate_chats(num_chats)
    return jsonify({"message": result})

# we generate messages for chats, 2 for each user, and the first message should be sent before the second and so on. (timestamp matters)
@app.route("/generate_messages", methods=["POST"])
def generate_messages():
    num_messages=4
    num_chats=25
    result = generators.generate_messages(num_chats, num_messages)  # Generate 4 messages (2 per user), for 25 chats
    return jsonify({"message": result})


if __name__ == "__main__":
    # Run the Flask app
    app.run(host="0.0.0.0", port=5000)

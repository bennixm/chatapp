from flask import Flask, jsonify
import mysql.connector
from flask_cors import CORS  # Import CORS

app = Flask(__name__)

# CORS configuration to allow requests from your Vue frontend
CORS(app, origins=["http://localhost:8081"])  # Only allow requests from frontend running on port 8081

@app.route("/stats", methods=["GET"])
def get_stats():
    # Establish MySQL connection
    connection = mysql.connector.connect(
        host="mysql",  # Use Docker MySQL container's name
        user="root",
        password="chatapp99",
        database="chatapp"
    )
    cursor = connection.cursor()

    # Example query to fetch statistics
    cursor.execute("SELECT COUNT(*) FROM messages")  # Assuming `messages` table exists
    result = cursor.fetchone()

    cursor.close()
    connection.close()

    return jsonify({"message_count": result[0]})

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)

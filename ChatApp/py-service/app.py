from flask import Flask, jsonify, request
from flask_cors import CORS
import mysql.connector
from datetime import datetime
import generators  # we take all the generators from generators.py
from getters import get_stats, get_messages, get_monthly_message_counts, get_message_sentiments  # we take all from getters
import json
import pandas as pd
import numpy as np
import requests
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
import threading

import logging

import matplotlib.pyplot as plt
from io import BytesIO
import base64
import seaborn as sns





app = Flask(__name__)
CORS(app, origins=["http://localhost:8081"])

@app.route("/stats", methods=["GET"])
def stats():
    # Fetch user count using the function from getters.py
    stats = get_stats()

    return jsonify(stats)

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
    generators.generate_users(first_names, last_names, num_users=10)

    return jsonify({"message": "10 users have been generated and inserted into the database!"})

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
    num_chats=50
    result = generators.generate_messages(num_chats, num_messages)  # Generate 4 messages (2 per user), for 50 chats
    return jsonify({"message": result})


@app.route("/get_messages", methods=["GET"])
def get_messages_endpoint():
    # Get sender_id from query parameters
    sender_id = request.args.get("sender_id", type=int)

    # Fetch messages based on sender_id
    messages = get_messages(sender_id)

    return jsonify({"messages": messages})



logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[logging.StreamHandler()]
)
model = None
def train_model():
    global model
    logging.info("Fetching messages to train the model...")
    try:
        response = requests.get("http://localhost:5001/get_messages")
        messages = response.json()["messages"]
    except Exception as e:
        logging.error(f"Error fetching messages: {e}")
        return

    if len(messages) < 2:
        logging.warning("Not enough data to train the model.")
        return

    # Convert messages to a DataFrame
    df = pd.DataFrame(messages)
    df["timestamp"] = pd.to_datetime(df["timestamp"])

    # Sort messages by sender and time
    df = df.sort_values(by=["sender_id", "timestamp"])

    # Compute response time (time difference between messages)
    df["response_time"] = df.groupby("sender_id")["timestamp"].diff().dt.total_seconds()

    # Ignore responses where the previous message was sent >1 day ago (but keep the info for model)
    df["is_long_gap"] = df["response_time"] > 86400  # 86400 seconds = 1 day
    df["response_time"] = df["response_time"].where(df["response_time"] <= 86400)

    # Drop NaN values (first message of a user has no previous message)
    df = df.dropna()

    if df.empty:
        logging.warning("No valid response times after filtering.")
        return

    # Add additional features for better prediction
    df["hour_of_day"] = df["timestamp"].dt.hour
    df["day_of_week"] = df["timestamp"].dt.dayofweek
    df["user_avg_response_time"] = df.groupby("sender_id")["response_time"].transform("mean")

    # Calculate the time since last message (time gap since user's last activity)
    df["time_since_last_message"] = df.groupby("sender_id")["timestamp"].diff().dt.total_seconds().fillna(0)

    # Filter out extreme long gaps (over 1 day)
    df = df[df["response_time"] <= 86400]

    # Define the list of features to be used for training
    features = ["sender_id", "hour_of_day", "day_of_week", "user_avg_response_time", "time_since_last_message", "is_long_gap"]

    # Ensure that all columns in `features` exist in the DataFrame
    missing_columns = [col for col in features if col not in df.columns]
    if missing_columns:
        logging.error(f"Missing columns in data: {', '.join(missing_columns)}")
        return

    X = df[features]
    y = df["response_time"]

    # Handle edge case: Not enough data for a meaningful split
    if len(X) > 1:
        logging.info("Splitting data into train/test sets...")
        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

        # Train the model
        model = RandomForestRegressor(n_estimators=100, random_state=42)
        model.fit(X_train, y_train)

        logging.info("Model trained successfully!")
        # Optional: Print feature importance to see what's influencing predictions
        logging.info(f"Feature Importances: {model.feature_importances_}")
    else:
        logging.warning("Not enough data to split into train/test sets.")


@app.route("/predict_response_time", methods=["POST"])
def predict_response_time():
    global model
    if model is None:
        return jsonify({"error": "Model is not trained yet."}), 503

    # Get the input data from the request
    data = request.json

    # Check if 'timestamp' exists in the request, if not, use current time
    if "timestamp" not in data:
        timestamp = datetime.now()
    else:
        try:
            timestamp = pd.to_datetime(data["timestamp"])  # Ensure it's a valid datetime
        except Exception as e:
            return jsonify({"error": f"Invalid timestamp format: {str(e)}"}), 400

    sender_id = data.get("sender_id")  # This assumes sender_id is required
    if not sender_id:
        return jsonify({"error": "sender_id is required"}), 400

    # Fetch historical messages for the sender (using the modified get_messages function)
    response = requests.get(f"http://localhost:5001/get_messages?sender_id={sender_id}")
    if response.status_code != 200:
        return jsonify({"error": "Failed to fetch messages for the sender."}), 500

    messages = response.json().get("messages", [])

    # Log the fetched messages for debugging purposes
    logging.info(f"Fetched {len(messages)} messages for sender_id: {sender_id}")

    # If there are no previous messages, we set default values
    if len(messages) == 0:
        user_avg_response_time = 0
        time_since_last_message = 0
        is_long_gap = False
    else:
        # Process the fetched messages as before
        df = pd.DataFrame(messages)
        df["timestamp"] = pd.to_datetime(df["timestamp"])
        df = df.sort_values(by=["sender_id", "timestamp"])

        df["response_time"] = df.groupby("sender_id")["timestamp"].diff().dt.total_seconds()
        df["time_since_last_message"] = df["timestamp"].diff().dt.total_seconds().fillna(0)

        # Calculate user-level features
        user_avg_response_time = df["response_time"].mean()
        time_since_last_message = df["time_since_last_message"].iloc[-1]
        is_long_gap = time_since_last_message > 86400  # 1 day = 86400 seconds

    # Prepare features and predict
    X_new = pd.DataFrame({
        "sender_id": [sender_id],
        "timestamp": [timestamp]
    })

    # Add features like hour of day, day of week, etc.
    X_new["hour_of_day"] = X_new["timestamp"].dt.hour
    X_new["day_of_week"] = X_new["timestamp"].dt.dayofweek
    X_new["user_avg_response_time"] = user_avg_response_time
    X_new["time_since_last_message"] = time_since_last_message
    X_new["is_long_gap"] = is_long_gap

    # Select features for prediction
    features = ["sender_id", "hour_of_day", "day_of_week", "user_avg_response_time", "time_since_last_message", "is_long_gap"]
    X_new = X_new[features]

    # Predict the response time
    try:
        prediction = model.predict(X_new)[0]
        return jsonify({"predicted_response_time": round(prediction, 2)})
    except Exception as e:
        return jsonify({"error": f"Prediction failed: {str(e)}"}), 500


@app.route("/train", methods=["GET"])
def train():
    try:
        threading.Thread(target=train_model).start()
        return jsonify({"message": "Model training started in the background!"})
    except Exception as e:
        return jsonify({"error": f"Failed to start model training: {str(e)}"}), 500

@app.route("/generate_graph", methods=["GET"])
def generate_graph():
    try:
        np.random.seed(42)

        # Generate dummy data (representing the features)
        num_samples = 1000
        sender_ids = np.random.choice([1, 2, 3, 4, 5], num_samples)
        timestamps = pd.date_range('2024-01-01', periods=num_samples, freq='h')
        response_times = np.random.gamma(2, 5, num_samples)
        hours_of_day = timestamps.hour
        day_of_week = timestamps.dayofweek

        # Create the DataFrame
        df = pd.DataFrame({
            'sender_id': sender_ids,
            'timestamp': timestamps,
            'response_time': response_times,
            'hour_of_day': hours_of_day,
            'day_of_week': day_of_week
        })

        # Calculate Open (first response time for each hour)
        open_values = df.groupby('hour_of_day')['response_time'].first().reset_index()

        # Calculate Close (last response time for each hour)
        close_values = df.groupby('hour_of_day')['response_time'].last().reset_index()

        # Calculate the mean and error margin (Interquartile range) for each hour
        response_time_by_hour = df.groupby('hour_of_day')['response_time'].describe().reset_index()

        # Calculate the error margin (Interquartile range)
        y_error_values = (response_time_by_hour['75%'] - response_time_by_hour['25%']).tolist()

        # Compute high and low for candlestick chart
        high_values = (response_time_by_hour['mean'] + (pd.Series(y_error_values) / 2)).tolist()  # Element-wise division
        low_values = (response_time_by_hour['mean'] - (pd.Series(y_error_values) / 2)).tolist()  # Element-wise division

        # Calculate volume (number of samples per hour)
        volume_by_hour = df.groupby('hour_of_day').size().tolist()

        # Prepare Graph 1 Data
        graph_1_data = {
            'x': response_time_by_hour['hour_of_day'].tolist(),
            'open': open_values['response_time'].tolist(),  # Open values (first response time)
            'close': close_values['response_time'].tolist(),  # Close values (last response time)
            'high': high_values,  # High (mean + error/2)
            'low': low_values,  # Low (mean - error/2)
            'volume': volume_by_hour  # Volume (number of samples)
        }

        # Graph 2: Response Time by Day of the Week
        response_time_by_day = df.groupby('day_of_week')['response_time'].describe().reset_index()
        graph_2_data = {
            'x': response_time_by_day['day_of_week'].tolist(),
            'y': response_time_by_day['mean'].tolist(),
            'y_error': (response_time_by_day['75%'] - response_time_by_day['25%']).tolist(),
            'labels': ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            'label': 'Response Time by Day of the Week'
        }

        # Graph 3: User's Average Response Time by Sender
        avg_response_time = df.groupby('sender_id')['response_time'].mean().reset_index()
        graph_3_data = {
            'x': avg_response_time['sender_id'].tolist(),
            'y': avg_response_time['response_time'].tolist(),
            'label': 'Average Response Time by Sender'
        }

        # Graph 4: Time Since Last Message vs Response Time
        df['time_since_last_message'] = df.groupby('sender_id')['timestamp'].diff().dt.total_seconds().fillna(0)
        graph_4_data = {
            'x': df['time_since_last_message'].tolist(),
            'y': df['response_time'].tolist(),
            'hue': df['sender_id'].tolist(),
            'label': "Time Since Last Message vs Response Time"
        }

        # Return the updated data for frontend
        return jsonify({
            "graph_1": graph_1_data,
            "graph_2": graph_2_data,
            "graph_3": graph_3_data,
            "graph_4": graph_4_data
        })

    except Exception as e:
        return jsonify({"error": str(e)}), 500





@app.route("/get_monthly_message_counts", methods=["GET"])
def get_monthly_message_counts_endpoint():
    try:
        monthly_counts = get_monthly_message_counts()
        return monthly_counts
    except Exception as e:
        return jsonify({"error": f"Failed to fetch monthly message counts: {str(e)}"}), 500

@app.route("/get_message_sentiments", methods=["GET"])
def get_message_sentiments_endpoint():
    try:
        logging.info("Request received for /get_message_sentiments")  # Add logging here
        messages_with_sentiment = get_message_sentiments()
        return jsonify(messages_with_sentiment)
    except Exception as e:
        logging.error(f"Error in /get_message_sentiments: {e}")  # More specific logging here
        return jsonify({"error": f"Failed to fetch message sentiments: {str(e)}"}), 500


if __name__ == "__main__":
    # Run the Flask app
    app.run(host="0.0.0.0", port=5001)
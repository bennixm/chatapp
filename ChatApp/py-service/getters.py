# getters.py
import logging
import json
from textblob import TextBlob

from db_conn import get_db_connection

import json

def get_stats():
    connection = get_db_connection()
    cursor = connection.cursor()

    # Get the count for each table
    cursor.execute("SELECT COUNT(*) FROM user")
    users_count = cursor.fetchone()[0]

    cursor.execute("SELECT COUNT(*) FROM messages")
    messages_count = cursor.fetchone()[0]

    cursor.execute("SELECT COUNT(*) FROM chats")
    chats_count = cursor.fetchone()[0]

    cursor.execute("SELECT COUNT(*) FROM friendship")
    friendships_count = cursor.fetchone()[0]

    cursor.close()
    connection.close()

    # Return as a dictionary instead of a JSON string
    stats_data = [
        {"stat": "users", "count": users_count},
        {"stat": "messages", "count": messages_count},
        {"stat": "chats", "count": chats_count},
        {"stat": "friendships", "count": friendships_count}
    ]

    return stats_data


logging.basicConfig(level=logging.INFO)



def get_messages(sender_id=None):
    connection = get_db_connection()
    cursor = connection.cursor(dictionary=True)

    if sender_id:
        query = "SELECT sender_id, chat_id, timestamp FROM messages WHERE sender_id = %s ORDER BY timestamp ASC"
        cursor.execute(query, (sender_id,))
    else:
        query = "SELECT sender_id, chat_id, timestamp FROM messages ORDER BY timestamp ASC"
        cursor.execute(query)

    messages = cursor.fetchall()

    connection.close()

    return messages


def get_monthly_message_counts():
    try:

        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        # Query to fetch messages from January 1, 2024, to December 31, 2024, grouped by month
        query = """
        SELECT MONTH(timestamp) AS month, COUNT(*) AS message_count
        FROM messages
        WHERE timestamp >= '2024-01-01' AND timestamp < '2025-01-01'
        GROUP BY MONTH(timestamp)
        ORDER BY month;
        """

        # Execute the query
        cursor.execute(query)

        # Fetch the results
        results = cursor.fetchall()

        # Map the month numbers to their respective names
        month_names = [
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        ]

        # Create a dictionary to store the results
        monthly_counts = {}
        for result in results:
            month_number = result['month']
            message_count = result['message_count']
            monthly_counts[month_names[month_number - 1]] = message_count

        cursor.close()
        connection.close()

        # Return the result as a JSON string
        return json.dumps(monthly_counts, indent=4)

    except Exception as e:
        # Log the full traceback to see exactly where the error occurred
        logging.error("An error occurred: %s", str(e))
        logging.error("Full traceback: %s", traceback.format_exc())

        # Return a generic error response
        return json.dumps({"error": "Internal Server Error", "message": str(e)}, indent=4)



def get_message_sentiments():
    logging.info("Starting to fetch message sentiments...")

    connection = get_db_connection()
    cursor = connection.cursor(dictionary=True)

    query = "SELECT sender_id, chat_id, timestamp, content FROM messages ORDER BY timestamp ASC"
    logging.info(f"Executing query: {query}")
    cursor.execute(query)

    messages = cursor.fetchall()
    logging.info(f"Fetched {len(messages)} messages for sentiment analysis from the database.")
    connection.close()

    def get_message_sentiment(message_text):
        try:
            blob = TextBlob(message_text)
            sentiment = blob.sentiment.polarity  # polarity is between -1 and 1
            logging.debug(f"Message: '{message_text}' Sentiment: {sentiment}")

            if sentiment >= 0.5:
                return 'very_positive'
            elif sentiment > 0 and sentiment < 0.5:
                return 'positive'
            elif sentiment == 0:
                return 'neutral'
            elif sentiment > -0.5 and sentiment < 0:
                return 'negative'
            elif sentiment <= -0.5:
                return 'very_negative'
        except Exception as e:
            logging.error(f"Error in analyzing sentiment for message: '{message_text}'. Error: {e}")
            return 'neutral'

    # Initialize counts for sentiment categories
    sentiment_counts = {
        "very_positive": 0,
        "positive": 0,
        "neutral": 0,
        "negative": 0,
        "very_negative": 0
    }

    # Analyze sentiment for each message and count
    for message in messages:
        # Change this to access 'content' instead of 'message_text'
        sentiment = get_message_sentiment(message['content'])
        sentiment_counts[sentiment] += 1
        logging.debug(f"Message ID {message['chat_id']} Sentiment: {sentiment}, Current Count: {sentiment_counts[sentiment]}")

    # Return the sentiment counts in the desired format
    sentiment_data = [
        {"sentiment": "very_positive", "count": sentiment_counts["very_positive"]},
        {"sentiment": "positive", "count": sentiment_counts["positive"]},
        {"sentiment": "neutral", "count": sentiment_counts["neutral"]},
        {"sentiment": "negative", "count": sentiment_counts["negative"]},
        {"sentiment": "very_negative", "count": sentiment_counts["very_negative"]}
    ]

    logging.info("Sentiment analysis complete.")
    logging.debug(f"Sentiment Data: {sentiment_data}")

    return sentiment_data

# getters.py
import logging

from db_conn import get_db_connection  # Import the database connection function

def get_user_count():
    connection = get_db_connection()
    cursor = connection.cursor()

    # Query to fetch the user count
    cursor.execute("SELECT COUNT(*) FROM user")
    result = cursor.fetchone()

    cursor.close()
    connection.close()

    return result[0]  # Return the user count



logging.basicConfig(level=logging.INFO)


# Updated get_messages function to fetch messages for a specific sender
def get_messages(sender_id=None):
    connection = get_db_connection()
    cursor = connection.cursor(dictionary=True)

    # Modify the query to include a condition for sender_id if it's provided
    if sender_id:
        query = "SELECT sender_id, chat_id, timestamp FROM messages WHERE sender_id = %s ORDER BY timestamp ASC"
        cursor.execute(query, (sender_id,))
    else:
        query = "SELECT sender_id, chat_id, timestamp FROM messages ORDER BY timestamp ASC"
        cursor.execute(query)

    logging.info("Fetching messages from database...")
    messages = cursor.fetchall()
    logging.info(f"Fetched {len(messages)} messages.")
    connection.close()

    return messages

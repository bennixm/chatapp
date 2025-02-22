import random
import bcrypt
import time
import openai
import openaiconfig
from datetime import datetime, timedelta
from db_conn import get_db_connection  # Import the connection function

# Function to generate random username
def generate_username(first_names, last_names):
    first_name = random.choice(first_names).lower()
    last_name = random.choice(last_names).lower()
    number = random.randint(10, 99)
    return f"{first_name}{last_name}{number}"

# Function to generate random email
def generate_email(username):
    return f"{username}@chatapp.com"

# Function to generate bcrypt encoded password
def generate_password():
    plain_password = "password123"
    return bcrypt.hashpw(plain_password.encode('utf-8'), bcrypt.gensalt()).decode('utf-8')

# Function to check if a user already exists (to avoid duplicates)
def check_if_exists(cursor, username, email):
    cursor.execute("SELECT * FROM user WHERE username = %s OR email = %s", (username, email))
    return cursor.fetchone()  # Returns None if no match is found

# Function to generate random date
def generate_random_date():
    start_date = datetime(2020, 1, 1)
    end_date = datetime(2024, 12, 31)
    delta = end_date - start_date
    random_days = random.randint(0, delta.days)
    return start_date + timedelta(days=random_days)

# Function to generate users
def generate_users(first_names, last_names, num_users=10):
    connection = get_db_connection()
    if not connection:
        print("Failed to connect to the database.")
        return

    cursor = connection.cursor()

    inserted_count = 0
    while inserted_count < num_users:
        username = generate_username(first_names, last_names)
        email = generate_email(username)
        password = generate_password()

        if check_if_exists(cursor, username, email):
            continue

        insert_query = "INSERT INTO user (username, email, password) VALUES (%s, %s, %s)"
        cursor.execute(insert_query, (username, email, password))
        inserted_count += 1

    connection.commit()
    cursor.close()
    connection.close()

# Function to generate friendships
def generate_friendships(num_friendships=50):
    connection = get_db_connection()
    if not connection:
        print("Failed to connect to the database.")
        return

    cursor = connection.cursor()

    cursor.execute("SELECT userid FROM user")
    users = cursor.fetchall()

    friendships_created = 0
    while friendships_created < num_friendships:
        user1 = random.choice(users)
        user2 = random.choice(users)

        if user1[0] != user2[0]:
            cursor.execute("""
                SELECT * FROM friendship WHERE (user1_id = %s AND user2_id = %s) OR (user1_id = %s AND user2_id = %s)
            """, (user1[0], user2[0], user2[0], user1[0]))
            existing_friendship = cursor.fetchone()

            if not existing_friendship:
                requester = random.choice([user1[0], user2[0]])
                created_at = generate_random_date()

                cursor.execute("""
                    INSERT INTO friendship (user1_id, user2_id, requested_by, created_at, status)
                    VALUES (%s, %s, %s, %s, %s)
                """, (user1[0], user2[0], requester, created_at, 1))

                connection.commit()
                friendships_created += 1

    cursor.close()
    connection.close()

# Helper function to check if a chat already exists
def chat_exists(cursor, user1_id, user2_id):
    cursor.execute("""
        SELECT 1 FROM chats WHERE
        (user1_id = %s AND user2_id = %s) OR
        (user1_id = %s AND user2_id = %s)
    """, (user1_id, user2_id, user2_id, user1_id))
    return cursor.fetchone() is not None

def generate_chats(num_chats):
    connection = get_db_connection()
    if not connection:
        print("Failed to connect to the database.")
        return

    cursor = connection.cursor()

    # Fetch all friendships where the status is 1 (accepted friendships)
    cursor.execute("SELECT user1_id, user2_id FROM friendship WHERE status = 1")
    friendships = cursor.fetchall()

    # Fetch the current number of chats
    cursor.execute("SELECT COUNT(*) FROM chats")
    existing_chats = cursor.fetchone()[0]

    # Calculate the maximum possible number of chats (equal to the number of friendships)
    max_possible_chats = len(friendships)

    # If all possible chats are already created, return an error message
    if existing_chats >= max_possible_chats:
        cursor.close()
        connection.close()
        return "Error: All possible chats are already created."

    created_chats = 0
    while created_chats < num_chats and friendships:
        user1_id, user2_id = random.choice(friendships)

        if not chat_exists(cursor, user1_id, user2_id):
            # Insert the chat into the chats table
            cursor.execute("""
                INSERT INTO chats (user1_id, user2_id)
                VALUES (%s, %s)
            """, (user1_id, user2_id))
            connection.commit()
            created_chats += 1

        # Remove the friendship to avoid duplicates
        friendships.remove((user1_id, user2_id))

    cursor.close()
    connection.close()

    return f"{created_chats} chats have been created."


openai.api_key = openaiconfig.get_openai_api_key()

def generate_messages(num_chats, num_messages):
    connection = get_db_connection()  # Assumes this function connects to your database
    if not connection:
        print("Failed to connect to the database.")
        return

    cursor = connection.cursor()

    # Fetch 25 random chats from the "chats" table
    cursor.execute("SELECT id, user1_id, user2_id FROM chats ORDER BY RAND() LIMIT %s", (num_chats,))
    chats = cursor.fetchall()

    total_messages_created = 0

    for chat in chats:
        chat_id, user1_id, user2_id = chat

        # Generate a conversation with OpenAI (4 messages - 2 per user)
        conversation = []
        for i in range(num_messages // 2):
            # Generate message 1 for user1
            user1_message = generate_message(user1_id, user2_id, conversation)
            conversation.append(user1_message)

            # Generate message 2 for user2 (response to user1)
            user2_message = generate_message(user2_id, user1_id, conversation)
            conversation.append(user2_message)

        # Generate a random starting date between 2022 and 2025
        start_date = generate_random_date()
        timestamp = start_date  # This will be the timestamp for the first message

        for i, message in enumerate(conversation):
            sender_id = user1_id if i % 2 == 0 else user2_id

            # Randomly decide if the time interval should be short (1-2 minutes) or longer (30-90 minutes)
            if random.random() < 0.3:  # 30% chance to shorten the interval (can adjust this probability)
                time_interval = random.randint(1, 2)  # 1-2 minutes
            else:
                time_interval = random.randint(30, 90)  # 30-90 minutes

            # Calculate the message's timestamp
            message_timestamp = timestamp + timedelta(minutes=time_interval)

            # Update the timestamp for the next message
            timestamp = message_timestamp

            # Insert the generated message into the "messages" table
            cursor.execute("""
                INSERT INTO messages (chat_id, sender_id, content, timestamp)
                VALUES (%s, %s, %s, %s)
            """, (chat_id, sender_id, message, message_timestamp))

            connection.commit()
            total_messages_created += 1

    cursor.close()
    connection.close()

    return f"{total_messages_created} messages have been generated and inserted into the database."


def generate_random_date():
    """Generate a random date between January 1, 2022 and December 31, 2025"""
    start_date = datetime(2024, 1, 1)
    end_date = datetime(2024, 12, 31)
    delta = end_date - start_date
    random_days = random.randint(0, delta.days)
    random_date = start_date + timedelta(days=random_days)

    return random_date


def generate_message(sender_id, receiver_id, conversation):
    """Generate a single message using OpenAI's GPT model without role labels, just plain text conversation."""

    # Refined system message to tell the model to generate one single message without the need for previous context
    messages = [
        {"role": "system", "content": "You are generating a casual conversation between two people. The response should be a single sentence or short exchange. No labels, roles, or quotes should be included. Just the text of the conversation, like a real chat between two people."}
    ]

    # Only use the most recent message to generate the next message (no earlier conversation)
    recent_conversation = conversation[-1:]  # Get only the last message

    # Add this recent message to the conversation context for the model
    for msg in recent_conversation:
        messages.append({"role": "user", "content": msg})

    retries = 5  # Number of retries before giving up
    for i in range(retries):
        try:
            # Use OpenAI's GPT model to generate the next part of the conversation
            response = openai.ChatCompletion.create(
                model="gpt-3.5-turbo",
                messages=messages,  # Provide only the most recent conversation as context
                max_tokens=150,  # Limit the length of the response
                temperature=0.9,  # Allow creative responses
                top_p=1.0,  # Wide range of possibilities
                frequency_penalty=0.5,  # Avoid repetitive phrases
                presence_penalty=0.5,  # Encourage new topics
            )

            # Extract the generated message (strip leading/trailing whitespace)
            message = response.choices[0].message['content'].strip()

            # Return the message without any labels or quotes
            return message

        except openai.error.RateLimitError as e:
            if i < retries - 1:
                print(f"Rate limit exceeded, retrying in {2 ** i} seconds...")
                time.sleep(2 ** i)  # Exponential backoff for retries
            else:
                print("Exceeded maximum retry attempts. Please check your OpenAI plan.")
                return "Error: Rate limit exceeded. Please try again later."

        except openai.error.InvalidRequestError as e:
            print(f"Invalid request: {e}")
            return "Error: Invalid request. Please check the parameters and try again."



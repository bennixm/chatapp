# getters.py
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

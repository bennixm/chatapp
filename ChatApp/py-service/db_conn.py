import mysql.connector
from mysql.connector import Error

def get_db_connection():
    """
    Establish and return a database connection.
    Returns:
        mysql.connector.connection.MySQLConnection: The database connection.
    """
    try:
        connection = mysql.connector.connect(
            host="mysql",
            user="root",
            password="chatapp99",
            database="chatapp"
        )
        if connection.is_connected():
            return connection
    except Error as e:
        print(f"Error: {e}")
        return None

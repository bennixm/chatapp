# Chat Application V1.0

This is a Chat Application built with **Vue.js**, **Spring Boot**, and **MySQL**. It allows users to register, log in, search for people, send and receive friendship requests, and chat with friends in real-time.

![Preview IMG](https://github.com/bennixm/chatapp/blob/5311c4f105b1232cb8037f513c784b4782ab5eb5/Screenshot%202025-02-19%20at%2009.05.38.png)

## Features
- **User Registration**: Users can create an account with their credentials.
- **Login System**: Registered users can log in to their accounts.
- **Search for People**: Users can search for other users to send friendship requests.
- **Friend Requests**: Users can send requests of friendship to other users.
- **Friendships**: Once a friendship request is accepted, users can see their friends in the sidebar.
- **Real-Time Chat**: Users can chat with their friends in real-time through the chat interface.

## Getting Started

### Prerequisites
Make sure you have the following installed:
- **Node.js** for the Vue.js frontend
- **MySQL** for the database
- **Java JDK** for running the Spring Boot backend ( for me i use SAP's JDK but you can use Java JDK -- check the config and edit there)

### Installation
     cd Chatapp
     ```

1. **Clone the repository**:
   ```bash
   git clone https://github.com/bennixm/chatapp.git
   cd chatapp
   
2. **Frontend Setup** (Vue.js):
   - Navigate to the `chat-app/` directory and install the required dependencies:
     ```bash
     cd chat-app
     npm install
     ```

3. **Backend Setup** (Spring Boot):
   - Navigate to the `backend/` directory and set up your MySQL database.
   - Create a `.env` file to store the necessary environment variables for the backend:
     - `SPRING_DATASOURCE_URL` = `jdbc:mysql://localhost:3306/chat_app`
     - `SPRING_DATASOURCE_USERNAME` = `your_mysql_username`
     - `SPRING_DATASOURCE_PASSWORD` = `your_mysql_password`
   - Build and run the Spring Boot application:
     ```bash
     cd backend
     ./mvnw spring-boot:run
     ```

4. **Run the frontend**:
   - Once the backend is running, open another terminal, go to the `chatapp/` directory, and run:
     ```bash
     npm run serve
     ```

   This will start the Vue.js frontend, and you can access the application on `http://localhost:8080`.

There is also posibility to run it with docker (if you have it setted) :  
  ```bash
     docker compose build
  ```
### Features in Detail

- **Registration**:
  - Users can register with their email and password.
  - Passwords are securely hashed and stored in the MySQL database.

- **Login**:
  - Users can log in using their registered credentials.
  - Upon successful login, a session is created.

- **Search and Add Friends**:
  - After logging in, users can search for other registered users.
  - They can send and receive friendship requests.

- **Sidebar**:
  - Once a friendship is accepted, the friend's name appears on the sidebar.
  - Users can click on the friend’s name to open the chat interface.

- **Real-Time Chat**:
  - Users can send and receive messages instantly.
  - The chat interface supports real-time message updates, and users will see new messages without refreshing the page.

### Technologies Used

- **Frontend**: Vue.js (for building the UI)
- **Backend**: Spring Boot (for handling authentication, friendship requests, and chat management)
- **Database**: MySQL (for storing user data, friendship relationships, and messages)
- **WebSocket**: For real-time communication between users

### Folder Structure

```
📦 chatapp
├─ chat-app (VueJS)
│  ├─ node_modules
│  ├─ service
DockerFile
package.json
jsconfig.json
│  ├─ src
│  │  ├─ App.vue
main.js
│  │  ├─ views
│  │  │  ├─ ChatPage.vue
│  │  │  ├─ Dashboard.vue
│  │  │  ├─ ChatWindow.vue
│  │  │  ├─ HomePage.vue
│  │  │  ├─ Login.vue
│  │  │  ├─ NewChat.vue
│  │  │  ├─ Search.vue
│  │  │  ├─ Register.vue
│  │  │  ├─ Settings.vue
│  │  │  └─ StatsPage.vue
│  │  ├─ router
│  │  │  └─ index.js
│  │  ├─ events
│  │  │  └─ eventBus.j
│  │  ├─ components
│  │  │  ├─ Footer.vue
│  │  │  └─ Nav.vue
│  │  └─ assets
│  │     ├─ font-awesome
│  │     ├─ images
│  │     └─ css
│  │        └─ style.cs
│  └─ public
│     └─ index.html
└─ backend (Java Spring)
   ├─ src
   │  └─ main
   │     └─ java
   │        └─ com
   │           └─ example
   │              └─ chatapp
   │                 ├─ Config
   │                 ├─ Dto
   │                 ├─ Entity
   │                 ├─ payload
   │                 ├─ Repository
   │                 ├─ Service
   │                 ├─ UserController
   │                 └─ ChatappApplication.java
   ├─ target
   │  └─ classes
   │     └─ com
   │        └─ application.propertie
   ├─ DockerFile
   └─ pom.xm
```
# Scientific Part Of Web Chat App
# Installation

```
📦 py-service
├─ app.py
├─ db_conn.py
├─ DockerFile
├─ generators.py
├─ openaiconfig.py
└─ requirements.txt
```

1. **Clone the repository**:

   ```bash
   git clone https://github.com/yourusername/chat-app.git
   cd chat-app

- **Install dependencies**:

    ```bash
    pip install -r requirements.txt
    ```

- **Set up the database**:  
    Ensure you have a MySQL database set up and configured. Update the database connection settings in `db_conn.py`.

- **Run the application**:

    ```bash
    python app.py
    ```

    The Flask app will start running on `http://localhost:5001`.

- **Running in Docker**:

    To build and run the application in Docker:

    ```bash
    docker build -t chat-app .
    docker run -p 5001:5001 chat-app
    ```


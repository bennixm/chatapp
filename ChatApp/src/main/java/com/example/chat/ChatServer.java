package com.example.chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static Set<ClientHandler> clientHandlers = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Server started...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected.");
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Broadcast message to all clients
    public static void broadcast(String message) {
        for (ClientHandler client : clientHandlers) {
            client.sendMessage(message);
        }
    }

    // Remove client from list
    public static void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }

    // ClientHandler for managing individual client connections
    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                // Get username
                out.println("Enter your username: ");
                username = in.readLine();
                out.println("Welcome " + username + "! You can start chatting.");
                ChatServer.broadcast(username + " has joined the chat.");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("bye")) {
                        break;
                    }
                    ChatServer.broadcast(username + ": " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                ChatServer.removeClient(this);
                ChatServer.broadcast(username + " has left the chat.");
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Send message to client
        public void sendMessage(String message) {
            out.println(message);
        }
    }
}

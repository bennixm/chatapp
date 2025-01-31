package com.example.chat;

import java.io.*;
import java.net.*;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    
    // Assuming the server is running on localhost and port 12345
    private String serverAddress = "localhost";
    private int port = 12345;
    
    public void connect() {
        try {
            socket = new Socket(serverAddress, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connected to server");
            
            // Listening for incoming messages in a separate thread
            new Thread(this::listenForMessages).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
    
    private void listenForMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                // Handle received message (update GUI or log)
                System.out.println("Server says: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.example.chat;

import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;

    public static void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public static void listenForMessages() {
        try {
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                ChatApp.appendMessage("Server: " + serverMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

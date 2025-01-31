package com.example.chat;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ChatApp extends Application {
    private static TextArea chatDisplay;
    private static ChatClient client;  // Assuming you have a ChatClient class to handle connections

    @Override
    public void start(Stage primaryStage) {
        chatDisplay = new TextArea();
        chatDisplay.setEditable(false);
        chatDisplay.setWrapText(true);

        TextField messageInput = new TextField();
        messageInput.setPromptText("Type your message here...");
        Button sendButton = new Button("Send");

        HBox inputArea = new HBox(10, messageInput, sendButton);
        HBox.setHgrow(messageInput, Priority.ALWAYS);  // Fix here, use HBox.setHgrow()

        VBox root = new VBox(10, chatDisplay, inputArea);
        root.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");

        sendButton.setOnAction(e -> {
            String message = messageInput.getText().trim();
            if (!message.isEmpty()) {
                appendMessage("You: " + message);
                messageInput.clear();
                client.sendMessage(message);  // Calling sendMessage from ChatClient
            }
        });

        primaryStage.setTitle("Chat Application");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();

        // Start a new thread to handle client connection
        new Thread(() -> client.connect()).start();  // Use lambda to wrap method call in Runnable
    }

    public static void appendMessage(String message) {
        chatDisplay.appendText(message + "\n");
    }

    public static void main(String[] args) {
        // Assuming ChatClient has a static method to initialize connection
        client = new ChatClient();  // Initialize the client
        launch(args);
    }
}

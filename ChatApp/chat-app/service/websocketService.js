import SockJS from "sockjs-client";
import { Client } from '@stomp/stompjs';

const socketUrl = 'http://localhost:8085/ws-chat';

let stompClient = null;

const WebSocketService = {
    connect: (chatId, onMessageReceived) => {
        const socket = new SockJS(socketUrl);
        stompClient = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                console.log("WebSocket Connected via SockJS!");
                stompClient.subscribe(`/topic/chat/${chatId}`, (message) => {
                    onMessageReceived(JSON.parse(message.body));
                });
            },
            onStompError: (error) => {
                console.error("STOMP Error:", error);
            },
            onWebSocketError: (error) => {
                console.error("WebSocket Error:", error);
            }
        });

        stompClient.activate();
    },

    sendMessage: (chatId, message) => {
        if (stompClient && stompClient.connected) {
            stompClient.publish({
                destination: `/app/chat/${chatId}`,
                body: JSON.stringify(message)
            });
        }
    },

    disconnect: () => {
        if (stompClient) {
            stompClient.deactivate();
        }
    }
};


export default WebSocketService;

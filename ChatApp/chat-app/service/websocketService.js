import { Client } from '@stomp/stompjs';

const socketUrl = 'ws://localhost:8085/ws-chat';

let stompClient = null;

const WebSocketService = {
    connect: (chatId, onMessageReceived) => {
        stompClient = new Client({
            brokerURL: socketUrl,
            onConnect: () => {
                stompClient.subscribe(`/topic/chat/${chatId}`, (message) => {
                    onMessageReceived(JSON.parse(message.body));
                });
            },
            onStompError: (error) => {
                console.error("WebSocket Error: ", error);
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

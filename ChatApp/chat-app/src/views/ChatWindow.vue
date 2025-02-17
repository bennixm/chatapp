<template>
  <div v-if="chat">
    <h2>Chat with {{ receiverUsername }}</h2>
    <div class="messages">
      <div v-for="message in messages" :key="message.id" class="message">
        <strong>{{ message.senderUsername }}:</strong> {{ message.content }}
      </div>
    </div>
    <input v-model="newMessage" @keyup.enter="sendMessage" placeholder="Type a message..." />
    <button @click="sendMessage">Send</button>
  </div>
</template>

<script>
import { ref, watch, onBeforeUnmount } from 'vue';
import axios from 'axios';
import { useStore } from 'vuex';
import WebSocketService from '../../service/websocketService.js';

export default {
  props: ["receiverId", "receiverUsername"],
  setup(props) {
    const store = useStore();
    const senderId = store.state.userId;
    const chat = ref(null);
    const messages = ref([]);
    const newMessage = ref("");
    let socketConnected = false;

    const fetchOrCreateChat = async () => {
      try {
        let response = await axios.get(`http://localhost:8085/api/chats/${senderId}/${props.receiverId}`, {
          headers: {
            'Accept': 'application/json'
          }
        });
        chat.value = response.data;
        fetchMessages();
        if (!socketConnected) {
          WebSocketService.connect(chat.value.chatId, (message) => {
            messages.value.push(message);
          });
          socketConnected = true;
        }
      } catch (error) {
        console.error("Error fetching/creating chat:", error);
      }
    };

    const fetchMessages = async () => {
      if (!chat.value) return;
      try {
        const response = await axios.get(`http://localhost:8085/api/messages/${chat.value.chatId}`);
        messages.value = response.data;
      } catch (error) {
        console.error("Error fetching messages:", error);
      }
    };

    const sendMessageToChat = async () => {
      if (!newMessage.value.trim()) return;

      const message = {
        chatId: chat.value.chatId,
        senderId: senderId,
        content: newMessage.value,
      };

      try {
        await axios.post(`http://localhost:8085/api/messages/send`, null, {
          params: { chatId: message.chatId, senderId: message.senderId, content: message.content },
        });

        WebSocketService.sendMessage(chat.value.chatId, message);

        newMessage.value = "";
      } catch (error) {
        console.error("Error sending message:", error);
      }
    };

    watch(() => props.receiverId, fetchOrCreateChat, { immediate: true });

    onBeforeUnmount(() => {
      if (socketConnected) {
        WebSocketService.disconnect();
      }
    });

    return {
      chat,
      messages,
      newMessage,
      sendMessage: sendMessageToChat,
    };
  }
};
</script>



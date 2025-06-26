<template>
  <div v-if="chat" class="chat-window-space chat-window">
    <div class="chat-head">
      <div class="chat-user-heading">
        <div class="chat-img"></div>
        <span class="chat-head-name">{{ receiverUsername }}</span>
      </div>
      <div class="chat-button-heading">
        <button @click="toggleTimeFormat">
          <i class="fa fa-clock-o" aria-hidden="true"></i>
        </button>
      </div>
    </div>
    <div class="chat-content messages">
      <div
          v-for="message in messages"
          :key="message.id"
          :class="{
          'message-received': message.senderUsername === receiverUsername,
          'message-sent': message.senderUsername !== receiverUsername
        }"
          class="message message-text"
      >
        {{ message.content }}
        <span class="message-time">{{ formatTimestamp(message.timestamp) }}</span>
      </div>
    </div>
    <div class="chat-bottom">
      <input class="chat-input" v-model="newMessage" @keyup.enter="sendMessage" placeholder="Type a message..." />
      <button class="chat-button" @click="sendMessage"><i class="fa fa-paper-plane-o" aria-hidden="true"></i></button>
    </div>
  </div>
</template>

<script>
import { ref, watch, nextTick } from 'vue';
import axios from 'axios';
import { useStore } from 'vuex';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import Swal from 'sweetalert2';
import { onBeforeRouteUpdate } from 'vue-router';

export default {
  props: ["receiverId"],
  setup(props) {
    const store = useStore();
    const senderId = store.state.userId;
    const sessionUsername = store.state.username;
    const chat = ref(null);
    const messages = ref([]);
    const newMessage = ref("");
    const receiverUsername = ref("");
    const showTime = ref(false); // State to toggle time format

    let stompClient = null;

    const connectWebSocket = () => {
      const socket = new SockJS('http://localhost:8085/ws-chat');
      stompClient = Stomp.over(socket);

      stompClient.connect({}, (frame) => {
        console.log('Connected to WebSocket:', frame);
        stompClient.subscribe(`/topic/messages/${chat.value.chatId}`, (messageOutput) => {
          const message = JSON.parse(messageOutput.body);
          messages.value.push(message);
          scrollToBottom();
        });
      });
    };

    const fetchOrCreateChat = async () => {
      try {
        let response = await axios.get(`http://localhost:8085/api/chats/${senderId}/${props.receiverId}`, {
          headers: {
            'Accept': 'application/json'
          }
        });
        chat.value = response.data;
        receiverUsername.value = chat.value.user1Username === sessionUsername ? chat.value.user2Username : chat.value.user1Username;
        fetchMessages();
        connectWebSocket();
      } catch (error) {
        console.error("Error fetching/creating chat:", error);
      }
    };

    const fetchMessages = async () => {
      if (!chat.value) return;
      try {
        const response = await axios.get(`http://localhost:8085/api/messages/${chat.value.chatId}`);
        messages.value = response.data;
        sortMessages();
        scrollToBottom();
      } catch (error) {
        console.error("Error fetching messages:", error);
      }
    };

    const sendMessageToChat = async () => {
      if (newMessage.value.length > 250) {
        Swal.fire({
          icon: 'error',
          title: 'Message too long',
          text: 'Your message cannot exceed 250 characters.',
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6",
          heightAuto:false
        });
        return;
      }

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
        scrollToBottom();
        newMessage.value = "";
      } catch (error) {
        console.error("Error sending message:", error);
      }
    };

    const formatTimestamp = (timestamp) => {
      const now = new Date();
      const messageTime = new Date(timestamp);
      const timeString = messageTime.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
      const dateString = messageTime.toLocaleDateString();

      if (showTime.value) {
        if (now.toDateString() === messageTime.toDateString()) {
          return `${dateString} ${timeString}`;
        }
        const yesterday = new Date(now);
        yesterday.setDate(now.getDate() - 1);
        if (yesterday.toDateString() === messageTime.toDateString()) {
          return `Yesterday, ${timeString}`;
        }
        return `${dateString} ${timeString}`;
      }

      if (now.toDateString() === messageTime.toDateString()) {
        return timeString;
      }

      const yesterday = new Date(now);
      yesterday.setDate(now.getDate() - 1);

      if (yesterday.toDateString() === messageTime.toDateString()) {
        return `Yesterday, ${timeString}`;
      }

      return dateString;
    };

    const sortMessages = () => {
      messages.value.sort((a, b) => {
        const timestampA = new Date(a.timestamp);
        const timestampB = new Date(b.timestamp);
        if (isNaN(timestampA) || isNaN(timestampB)) {
          console.error("Invalid timestamp:", a.timestamp, b.timestamp);
          return 0;
        }
        return timestampA - timestampB;
      });
    };

    const scrollToBottom = () => {
      nextTick(() => {
        const chatContent = document.querySelector('.chat-content');
        if (chatContent) {
          chatContent.scrollTop = chatContent.scrollHeight;
        }
      });
    };

    const toggleTimeFormat = () => {
      showTime.value = !showTime.value;
    };

    watch(showTime, () => {
      messages.value = [...messages.value];
    });


    onBeforeRouteUpdate(async (to, from, next) => {
      await fetchOrCreateChat();
      next();
    });

    watch(() => props.receiverId, fetchOrCreateChat, { immediate: true });

    return {
      chat,
      messages,
      newMessage,
      sendMessage: sendMessageToChat,
      formatTimestamp,
      receiverUsername,
      toggleTimeFormat, // Expose the toggle method
    };
  }
};
</script>

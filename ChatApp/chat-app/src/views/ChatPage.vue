<template>
  <section>
    <h2>ChaPage</h2>
  </section>
  <div>
    <div v-for="message in messages" :key="message.id">
      <p>{{ message.user }}: {{ message.content }}</p>
    </div>
    <input v-model="newMessage" @keyup.enter="sendMessage" />
  </div>
</template>

<script>
import io from 'socket.io-client';

export default {
name: 'ChatPage',
    props: {
      msg: String
},
  data() {
    return {
      socket: null,
      messages: [],
      newMessage: ''
    };
  },
  created() {
    this.socket = io('http://localhost:8080');
    this.socket.on('chat-message', (message) => {
      this.messages.push(message);
    });
  },
  methods: {
    sendMessage() {
      if (this.newMessage.trim() !== '') {
        const message = {
          user: 'User1',
          content: this.newMessage
        };
        this.socket.emit('send-message', message);
        this.newMessage = '';
      }
    }
  }
};
</script>

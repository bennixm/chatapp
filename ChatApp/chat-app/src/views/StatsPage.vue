<template>
  <div class="panel-page">
    <div class="header-page">
      <span class="title-page">Statistics</span>
    </div>
    <div class="content-page">
      <p>Users Count: {{ user_count }}</p>
      <div>
        <button @click="generateUsers">Generate 50 Users</button>
        <p v-if="message">{{ message }}</p>
        <button @click="generateFriendships">Generate 25 Friendships</button>
        <p v-if="message_friendship">{{ message_friendship }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'StatsPage',
  data() {
    return {
      user_count: 0,
      message: "",
      message_friendship: "",
    };
  },
  methods: {
    async generateUsers() {
      try {
        const response = await fetch("http://localhost:5001/generate_users", {
          method: "POST",
        });
        const data = await response.json();
        this.message = data.message;
      } catch (error) {
        console.error("Error generating users:", error);
        this.message = "Failed to generate users.";
      }
    },
    async generateFriendships() {
      try {
        const response = await fetch("http://localhost:5001/generate_friendships", {
          method: "POST",
        });
        const data = await response.json();

        this.message_friendship = data.message;
      } catch (error) {
        console.error("Error generating friendships:", error);
        this.message_friendship = "Failed to generate friendships.";
      }
    }
  },
  mounted() {
    axios.get('http://localhost:5001/stats')
        .then(response => {
          this.user_count = response.data.user_count;
        })
        .catch(error => {
          console.error("There was an error fetching the statistics:", error);
        });
  }
};
</script>

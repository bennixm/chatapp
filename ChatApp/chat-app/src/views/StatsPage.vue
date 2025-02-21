<template>
  <div class="panel-page">
      <div class="header-page">
        <span class="title-page">Statistics</span>
      </div>
    <div class="stats-content">
      <div class="content-page">
        <p>Users Count: {{ user_count }}</p>
      </div>
      <div class="search-nav">
        <button @click="generateUsers">Generate 50 Users</button>
        <button @click="generateFriendships">Generate 50 Friendships</button>
        <button @click="generateChats">Generate 50 Chats</button>
        <button @click="generateMessages">Generate 100 Messages</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import Swal from "sweetalert2";

export default {
  name: 'StatsPage',
  data() {
    return {
      user_count: 0,
    };
  },
  methods: {
    async generateUsers() {
      try {
        const response = await fetch("http://localhost:5001/generate_users", {
          method: "POST",
        });
        const data = await response.json();
        Swal.fire({
          icon: "success",
          title: "Success!",
          text: data.message,
          timer: 2000,
          showConfirmButton: false,
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6",
          heightAuto:false
        });
      } catch (error) {
        Swal.fire({
          icon: "error",
          title: "Fail!",
          text: "Failed to generate users..",
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6",
          heightAuto:false
        });
      }
    },
    async generateFriendships() {
      try {
        const response = await fetch("http://localhost:5001/generate_friendships", {
          method: "POST",
        });
        const data = await response.json();
        Swal.fire({
          icon: "success",
          title: "Success!",
          text: data.message,
          timer: 2000,
          showConfirmButton: false,
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6",
          heightAuto:false
        });
      } catch (error) {
        Swal.fire({
          icon: "error",
          title: "Fail!",
          text: "Failed to generate friendships",
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6",
          heightAuto:false
        });
      }
    },
    async generateChats() {
      try {
        const response = await fetch("http://localhost:5001/generate_chats", {
          method: "POST",
        });
        const data = await response.json();
        Swal.fire({
          icon: "success",
          title: "Success!",
          text: data.message,
          timer: 2000,
          showConfirmButton: false,
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6",
          heightAuto:false
        });
      } catch (error) {
        Swal.fire({
          icon: "error",
          title: "Fail!",
          text: "Failed to generate chats",
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6",
          heightAuto:false
        });
      }
    },
    async generateMessages() {
      try {
        const response = await fetch("http://localhost:5001/generate_messages", {
          method: "POST",
        });
        const data = await response.json();
        Swal.fire({
          icon: "success",
          title: "Success!",
          text: data.message,
          timer: 2000,
          showConfirmButton: false,
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6",
          heightAuto:false
        });
      } catch (error) {
        Swal.fire({
          icon: "error",
          title: "Fail!",
          text: "Failed to generate chats",
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6",
          heightAuto:false
        });
      }
    }
  },
  mounted() {
    axios.get('http://localhost:5001/stats')
        .then(response => {
          this.user_count = response.data.user_count;
        })
        .catch(error => {
          Swal.fire({
            icon: "error",
            title: "Fail!",
            text: "There was an error fetching the statistics:" +  error,
            customClass: {
              popup: "custom-popup",
            },
            background: "#000000e0",
            color: "#ffffff",
            confirmButtonColor: "#009ca6",
            heightAuto:false
          });
        });
  }
};
</script>

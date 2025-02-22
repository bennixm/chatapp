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
        <button @click="generateUsers">Generate 10 Users</button>
        <button @click="generateFriendships">Generate 50 Friendships</button>
        <button @click="generateChats">Generate 50 Chats</button>
        <button @click="generateMessages">Generate 200 Messages</button>
      </div>
    </div>
    <div class="stats-content">
      <div class="content-page">
        <h1 class="title">Chat Response Time Predictor</h1>

        <button @click="trainModel" class="train-btn" :disabled="isTraining">
          {{ isTraining ? "Training in Progress..." : "Train Model" }}
        </button>

        <form @submit.prevent="predictResponseTime" class="form">
          <label>User ID:</label>
          <input v-model="sender_id" type="number" required />

          <button type="submit" class="predict-btn">Predict Response Time</button>
        </form>

        <div v-if="prediction !== null" class="result">
          <h2>Predicted Response Time: {{ formattedPrediction }}</h2>
        </div>
      </div>
      <div class="search-nav">
      </div>
    </div>
    <div class="stats-content">
      <div class="content-page">
        <div v-if="graphs" class="graphs-container">
          <div class="graph" @click="openFullScreenPreview(graphs.graph_1)">
            <h3>Response Time by Hour of the Day</h3>
            <img :src="'data:image/png;base64,' + graphs.graph_1" alt="Response Time by Hour of the Day" />
          </div>
          <div class="graph" @click="openFullScreenPreview(graphs.graph_2)">
            <h3>Response Time by Day of the Week</h3>
            <img :src="'data:image/png;base64,' + graphs.graph_2" alt="Response Time by Day of the Week" />
          </div>
          <div class="graph" @click="openFullScreenPreview(graphs.graph_3)">
            <h3>Average Response Time by Sender</h3>
            <img :src="'data:image/png;base64,' + graphs.graph_3" alt="Average Response Time by Sender" />
          </div>
          <div class="graph" @click="openFullScreenPreview(graphs.graph_4)">
            <h3>Time Since Last Message vs Response Time</h3>
            <img :src="'data:image/png;base64,' + graphs.graph_4" alt="Time Since Last Message vs Response Time" />
          </div>
        </div>
      </div>
      <div class="search-nav">
        <h1 class="title">Response Time Graphs</h1>
        <button @click="fetchGraphs">Generate Graphs</button>
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
      sender_id: "",
      prediction: null,
      isTraining: false,
      graphs: null,
    };
  },
  computed:{
    formattedPrediction() {
      if (this.prediction === null) return "";
      const minutes = Math.floor(this.prediction / 60);
      const seconds = Math.round(this.prediction % 60);
      return minutes > 0 ? `${minutes} min ${seconds} sec` : `${seconds} sec`;
    }
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
    },
    async trainModel() {
      this.isTraining = true;
      try {
        const response = await axios.get("http://localhost:5001/train");
        if (response.data.message) {
          Swal.fire({ icon: "success", title: "Model Training Started", text: "Training is in progress.", timer: 2000, showConfirmButton: false });
        }
      } catch (error) {
        Swal.fire({ icon: "error", title: "Training Failed", text: "Error occurred while training." });
      } finally {
        this.isTraining = false;
      }
    },
    async predictResponseTime() {
      if (!this.sender_id) {
        Swal.fire({ icon: "warning", title: "Missing Input", text: "Please enter a User ID." });
        return;
      }
      try {
        const response = await axios.post("http://localhost:5001/predict_response_time", { sender_id: this.sender_id });
        this.prediction = Math.round(response.data.predicted_response_time);
      } catch (error) {
        Swal.fire({ icon: "error", title: "Prediction Failed", text: "Ensure the model is trained and input is correct." });
      }
    },
    async fetchGraphs() {
      try {
        const response = await axios.get("http://localhost:5001/generate_graph");
        if (response.data) {
          this.graphs = response.data;
        }
      } catch (error) {
        Swal.fire({
          icon: "error",
          title: "Failed to Fetch Graphs",
          text: "There was an error generating the graphs.",
        });
      }
    },
    openFullScreenPreview(graph) {
      Swal.fire({
        title: 'Full-Screen Graph Preview',
        html: `<img src="data:image/png;base64,${graph}" class="full-screen-preview" />`,
        showCloseButton: true,
        showConfirmButton: false,
        width: '100%',
        height: '100%',
        padding: 0,
        customClass: {
          popup: 'no-border'
        }
      });
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

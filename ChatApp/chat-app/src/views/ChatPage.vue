<template>
  <section>
    <h2>ChaPage</h2>
  </section>
  <div>
    <h1>{{ message }}</h1>
    <button @click="fetchHello">Fetch Hello from Backend</button>
  </div>
</template>

<script>
import axios from 'axios';


export default {
  name: 'ChatPage',
    props: {
      msg: String
},
data() {
  return {
    message: 'Welcome to Vue.js!',
  };
},
methods: {
  async fetchHello() {
    try {
      const response = await axios.get('http://backend:8080/api/hello');
      this.message = response.data;  // Update message with the backend response
    } catch (error) {
      console.error('There was an error!', error);
    }
  },
},
};
</script>

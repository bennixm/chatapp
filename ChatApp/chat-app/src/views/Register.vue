<template>
  <div class="card" align="left">
    <div class="card-header">Register Form</div>
    <div class="card-body">
      <form @submit.prevent="saveData">
        <label>Employee Name</label>
        <input
            type="text"
            v-model="user.username"
            name="username"
            id="username"
            class="form-control"
        />
        <label>Email</label>
        <input
            type="email"
            v-model="user.email"
            name="email"
            id="email"
            class="form-control"
        />
        <label>Password</label>
        <input
            type="password"
            v-model="user.password"
            name="password"
            id="password"
            class="form-control"
        />
        <input type="submit" value="Save" class="btn btn-success" />
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'RegisterPage',
  data() {
    return {
      result: {},
      user: {
        username: '',
        email: '',
        password: ''
      }
    };
  },
  created() {},
  mounted() {
    console.log("mounted() called.......");
  },
  methods: {
    saveData() {
      // Basic validation
      if (!this.user.username || !this.user.email || !this.user.password) {
        alert("Please fill in all fields.");
        return; // Don't proceed with the request
      }

      // You could add email validation here too if needed:
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(this.user.email)) {
        alert("Please enter a valid email address.");
        return;
      }

      // Password length validation (optional, based on your needs)
      if (this.user.password.length < 6) {
        alert("Password must be at least 6 characters long.");
        return;
      }

      // Now make the request
      axios
          .post("http://backend:8085/api/v1/user/save", this.user)
          .then(({ data }) => {
            console.log(data);
            alert("User registered successfully");
          })
          .catch(error => {
            console.error("There was an error!", error);
            alert("Error during registration");
          });
    }
  }
};
</script>

<style scoped>
</style>

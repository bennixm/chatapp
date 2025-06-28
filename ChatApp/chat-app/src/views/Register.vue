<template>
  <div class="panel">
    <div class="box">
      <span class="borderLine"></span>
      <form @submit.prevent="saveData">
        <h2>Sign up</h2>
        <div class="inputBox">
          <input type="text" v-model="user.username" required />
          <span>Username</span>
          <i></i>
        </div>
        <div class="inputBox">
          <input type="email" v-model="user.email" required />
          <span>Email</span>
          <i></i>
        </div>
        <div class="inputBox">
          <input type="password" v-model="user.password" required />
          <span>Password</span>
          <i></i>
        </div>
        <div class="links">
          <router-link to="/login">Sign in</router-link>
        </div>
        <button type="submit" class="btn btn-primary">Register</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import {
  showSuccessAlert,
  showErrorAlert,
  showWarningAlert,
  showInfoAlert
} from '@/utils/alertService';

export default {
  name: 'RegisterPage',
  data() {
    return {
      user: {
        username: '',
        email: '',
        password: ''
      }
    };
  },
  methods: {
    async saveData() {
      const { username, email, password } = this.user;

      if (!username || !email || !password) {
        await showWarningAlert("Missing Fields", "Please fill in all fields.");
        return;
      }

      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(email)) {
        await showWarningAlert("Invalid Email", "Please enter a valid email address.");
        return;
      }

      if (password.length < 6) {
        await showWarningAlert("Weak Password", "Password must be at least 6 characters long.");
        return;
      }

      try {
        const response = await axios.post("http://localhost:8085/api/v1/user/save", this.user);
        await showSuccessAlert("Registration Successful!", response.data.message || "You can now log in.");
        setTimeout(() => {
          this.$router.push({ path: "/login" });
        }, 2000);
      } catch (error) {
        console.error("Registration error:", error);

        if (error.response) {
          const errorMessage = error.response.data.message;
          let title = "Error";
          let text = "An error occurred during registration. Please try again.";
          let alertFunc = showErrorAlert;

          switch (errorMessage) {
            case "Email already exists":
              title = "Email Already Exists";
              text = "The email you entered is already registered. Please try using a different email.";
              alertFunc = showWarningAlert;
              break;
            case "Username already exists":
              title = "Username Already Exists";
              text = "The username you entered is already taken. Please try using a different username.";
              alertFunc = showWarningAlert;
              break;
            case "We have found an existing user":
              title = "Login";
              text = "We have found an existing user. Try to log in.";
              alertFunc = showInfoAlert;
              setTimeout(() => {
                this.$router.push({ path: "/login" });
              }, 2000);
              break;
          }

          await alertFunc(title, text);
        }
      }
    }
  }
};
</script>

<style scoped>
/* Add your scoped styles here */
</style>

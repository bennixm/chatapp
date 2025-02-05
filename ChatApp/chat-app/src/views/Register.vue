<template>
  <div class="panel">
    <div class="box">
      <span class="borderLine"></span>
      <form @submit.prevent="saveData">
        <h2>Sign up</h2>
        <div class="inputBox">
          <input type="text" v-model="user.username" name="username" id="username" class="form-control" required>
          <span>Username</span>
          <i></i>
        </div>
        <div class="inputBox">
          <input type="email" v-model="user.email" name="email" id="email" class="form-control" required>
          <span>Email</span>
          <i></i>
        </div>
        <div class="inputBox">
          <input type="password" v-model="user.password" name="password" id="password" class="form-control" required>
          <span>Password</span>
          <i></i>
        </div>
        <div class="links">
          <router-link to="/login"> Sign in</router-link>
        </div>
        <button type="submit" id="submit" class="btn btn-primary">Register</button>
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
      if (!this.user.username || !this.user.email || !this.user.password) {
        alert("Please fill in all fields.");
        return;
      }


      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(this.user.email)) {
        alert("Please enter a valid email address.");
        return;
      }


      if (this.user.password.length < 6) {
        alert("Password must be at least 6 characters long.");
        return;
      }


      axios
          .post("http://0.0.0.0:8085/api/v1/user/save", this.user)
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

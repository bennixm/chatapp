<template>
  <div class="panel">
    <div class="box">
      <span class="borderLine"></span>
      <form @submit.prevent="LoginData">
        <h2>Sign in</h2>
        <div class="inputBox">
          <input type="email" v-model="user.email" class="form-control" required />
          <span>Email</span>
          <i></i>
        </div>
        <div class="inputBox">
          <input type="password" v-model="user.password" class="form-control" required />
          <span>Password</span>
          <i></i>
        </div>
        <div class="links">
          <router-link to="/password-recovery">Forgot Password</router-link>
          <router-link to="/register">Sign up</router-link>
        </div>
        <button type="submit" id="submit" class="btn btn-primary">Login</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import { reactive } from 'vue';
import {
  showSuccessAlert,
  showErrorAlert
} from '@/utils/alertService';

export default {
  name: 'LoginPage',
  setup() {
    const store = useStore();
    const router = useRouter();

    const user = reactive({
      email: '',
      password: ''
    });

    const LoginData = async () => {
      try {
        const response = await axios.post(
            "http://localhost:8085/api/v1/user/login",
            user,
            { withCredentials: true }
        );

        if (response.data.username && response.data.userId) {
          await showSuccessAlert("Login Successful!", "Welcome back!");

          setTimeout(() => {
            store.commit('setUsername', response.data.username);
            store.commit('setUserId', response.data.userId);
            router.push({ name: 'DashboardPage' });
          }, 2000);
        } else {
          await showErrorAlert(
              "Login Failed",
              response.data.message || "Incorrect Email or Password!"
          );
        }

      } catch (error) {
        const status = error.response?.status;
        const message = error.response?.data?.message;

        if (status === 401) {
          await showErrorAlert("Unauthorized", message || "Incorrect Email or Password!");
        } else if (status === 500) {
          await showErrorAlert("Server Error", "Something went wrong on our end. Please try again later.");
        } else if (status === 404) {
          await showErrorAlert("Not Found", "The requested resource was not found.");
        } else {
          console.error("An error occurred:", error);
          await showErrorAlert("Error", "Something went wrong, please try again.");
        }
      }
    };

    return { user, LoginData };
  }
};
</script>

<style scoped>
/* Add your styles here */
</style>

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
import Swal from 'sweetalert2';

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
        const response = await axios.post("http://localhost:8085/api/v1/user/login", user);

        if (response.data.message === "Login Success") {

          Swal.fire({
            icon: "success",
            title: "Login Successful!",
            text: "Welcome back!",
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

          setTimeout(() => {
            store.commit('setUsername', response.data.username);
            localStorage.setItem('username', response.data.username);
            router.push({ name: 'ChatPage' });
          }, 2000);

        } else {

          Swal.fire({
            icon: "error",
            title: "Login Failed",
            text: response.data.message || "Incorrect Email or Password!",
            customClass: {
              popup: "custom-popup",
            },
            background: "#000000e0",
            color: "#ffffff",
            confirmButtonColor: "#009ca6",
            heightAuto:false
          });
        }

      } catch (error) {

        if (error.response && error.response.status === 401) {

          const errorMessage = error.response.data.message || "Incorrect Email or Password!";
          Swal.fire({
            icon: "error",
            title: "Unauthorized",
            text: errorMessage,
            customClass: {
              popup: "custom-popup",
            },
            background: "#000000e0",
            color: "#ffffff",
            confirmButtonColor: "#009ca6",
            heightAuto:false
          });

        } else if (error.response && error.response.status === 500) {

          Swal.fire({
            icon: "error",
            title: "Server Error",
            text: "Something went wrong on our end. Please try again later.",
            customClass: {
              popup: "custom-popup",
            },
            background: "#000000e0",
            color: "#ffffff",
            confirmButtonColor: "#009ca6",
            heightAuto:false
          });

        } else if (error.response && error.response.status === 404) {

          Swal.fire({
            icon: "error",
            title: "Not Found",
            text: "The requested resource was not found.",
            customClass: {
              popup: "custom-popup",
            },
            background: "#000000e0",
            color: "#ffffff",
            confirmButtonColor: "#009ca6",
            heightAuto:false
          });
        } else {

          console.error("An error occurred:", error);
          Swal.fire({
            icon: "error",
            title: "Error",
            text: "Something went wrong, please try again.",
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
    };

    return { user, LoginData };
  }
};
</script>

<style scoped>
/* Add your styles here */
</style>

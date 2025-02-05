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
          <router-link to="/password-recovery"> Forgot Password</router-link>
          <router-link to="/register"> Sign up</router-link>
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
        const response = await axios.post("http://0.0.0.0:8085/api/v1/user/login", user);
        console.log(response.data);

        if (response.data.message === "Login Success") {

          store.commit('setUsername', response.data.username);

          localStorage.setItem('username', response.data.username);


          router.push({ name: 'ChatPage' });
        } else {
          alert("Incorrect Email or Password.");
        }
      } catch (error) {
        console.error("An error occurred:", error);
        alert("Error, please try again.");
      }
    };


    return { user, LoginData };
  }
};
</script>

<style scoped>
/* Add your styles here */
</style>

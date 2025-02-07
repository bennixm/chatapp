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
          <router-link to="/login">Sign in</router-link>
        </div>
        <button type="submit" id="submit" class="btn btn-primary">Register</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import Swal from 'sweetalert2';

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
  mounted() {
    console.log("RegisterPage mounted...");
  },
  methods: {
    async saveData() {

      if (!this.user.username || !this.user.email || !this.user.password) {
        Swal.fire({
          icon: "warning",
          title: "Missing Fields",
          text: "Please fill in all fields.",
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6"
        });
        return;
      }

      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(this.user.email)) {
        Swal.fire({
          icon: "warning",
          title: "Invalid Email",
          text: "Please enter a valid email address.",
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6"
        });
        return;
      }

      if (this.user.password.length < 6) {
        Swal.fire({
          icon: "warning",
          title: "Weak Password",
          text: "Password must be at least 6 characters long.",
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6"
        });
        return;
      }

      try {
        const response = await axios.post("http://localhost:8085/api/v1/user/save", this.user);
        console.log(response.data);

        Swal.fire({
          icon: "success",
          title: "Registration Successful!",
          text: "You can now log in.",
          timer: 2000,
          showConfirmButton: false,
          customClass: {
            popup: "custom-popup",
          },
          background: "#000000e0",
          color: "#ffffff",
          confirmButtonColor: "#009ca6"
        });

        setTimeout(() => {
          this.$router.push({ path: "/login" });
        }, 2000);

      } catch (error) {
        console.error("Registration error:", error);

        if (error.response) {
          const errorMessage = error.response.data.message;

          if (errorMessage === "Email already exists") {
            Swal.fire({
              icon: "warning",
              title: "Email Already Exists",
              text: "The email you entered is already registered. Please try using a different email.",
              customClass: {
                popup: "custom-popup",
              },
              background: "#000000e0",
              color: "#ffffff",
              confirmButtonColor: "#009ca6"
            });
          } else if (errorMessage === "Username already exists") {
            Swal.fire({
              icon: "warning",
              title: "Username Already Exists",
              text: "The username you entered is already taken. Please try using a different username.",
              customClass: {
                popup: "custom-popup",
              },
              background: "#000000e0",
              color: "#ffffff",
              confirmButtonColor: "#009ca6"
            });
            } else if (errorMessage === "We have found an existing user") {
            Swal.fire({
              icon: "warning",
              title: "Login",
              text: "We have found an existing user, try to login.",
              customClass: {
                popup: "custom-popup",
              },
              background: "#000000e0",
              color: "#ffffff",
              confirmButtonColor: "#009ca6"
            });
            setTimeout(() => {
              this.$router.push({ path: "/login" });
            }, 2000);
            } else {
            Swal.fire({
              icon: "error",
              title: "Error",
              text: "An error occurred during registration. Please try again.",
              customClass: {
                popup: "custom-popup",
              },
              background: "#000000e0",
              color: "#ffffff",
              confirmButtonColor: "#009ca6"
            });
          }
        }
      }
    }
  }
};
</script>


<style scoped>
/* Add your styles here */
</style>

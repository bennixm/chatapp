<template>

  <div class="row">

    <div class="col-sm-4" >
      <h2 align="center"> Login</h2>

      <form @submit.prevent="LoginData">


        <div class="form-group" align="left">
          <label>Email</label>
          <input type="email" v-model="user.email" class="form-control"  placeholder="Email">
        </div>


        <div class="form-group" align="left">
          <label>Password</label>
          <input type="password" v-model="user.password" class="form-control"  placeholder="Password">
        </div>
        <br/>

        <button type="submit" class="btn btn-primary">Login</button>
      </form>
    </div>
  </div>

</template>

<script>

import axios from 'axios';

export default {
  name: 'LoginPage',
  data () {
    return {
      result: {},
      user:{
        email: '',
        password: ''
      }
    }
  },
  created() {
  },
  mounted() {
    console.log("mounted() called.......");
  },
  methods: {
    LoginData() {
      axios.post("http://backend:8085/api/v1/user/login", this.user)
          .then(({ data }) => {
            console.log(data);

            // Check for specific response messages
            if (data.message === "Email not exists") {
              alert("Email does not exist.");
            } else if (data.message === "Login Success") {
              this.$router.push({ name: 'HomePage' });
            } else {
              alert("Incorrect Email or Password.");
            }
          })
          .catch(err => {
            console.error("An error occurred:", err);
            alert("Error, please try again.");
          });
    }
  }
}
</script>
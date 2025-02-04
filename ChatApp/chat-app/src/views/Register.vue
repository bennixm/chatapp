<template>
  <div class="card" align="left">
    <div class="card-header">Register Form</div>
    <div class="card-body">

      <form  @submit.prevent="saveData">

        <label>Employee Name</label>
        <input type="text" v-model="user.username" name="username" id="username" class ="form-control"/>


        <label>Email</label>
        <input type="email" v-model="user.email" name="email" id="email" class ="form-control"/>

        <label>Password</label>
        <input type="password" v-model="user.password" name="password" id="password" class ="form-control"/>


        <input type="submit" value="Save" class="btn btn-success">


      </form>
    </div>
  </div>
</template>

<script>
import Vue from 'vue';
import axios from 'axios';
Vue.use(axios)


export default {
  name: 'RegisterPage',
  data () {
    return {
      result: {},
      user:{
        username: '',
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
    saveData()
    {
      axios.post("http://localhost:8085/api/v1/user/save", this.user)
          .then(
              ({data})=>{
                console.log(data);
                try
                {
                  alert("User reggistered Successfully");

                }
                catch(err)
                {
                  alert("failed");
                }
              }
          )
    }
  }
}
</script>

<style scoped>

</style>

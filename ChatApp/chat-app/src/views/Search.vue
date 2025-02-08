<template>
  <div class="panel-page">
    <div class="header-page">
      <span class="title-page">Find people</span>
    </div>
    <div class="content-page">
      <div class="search-section">
            <div class="search-section-content" v-if="users.length">
              <div class="display-user-item" v-for="user in users" :key="user.userid">
                <div class="user-item-identity"></div>
                <div class="user-item-content"><span class="username">{{ user.username }}</span><span class="email">{{ user.email }}</span></div>
                <div class="user-item-buttons">
                  <router-link to="/new-group"><i class="fa fa-user-plus" aria-hidden="true"></i></router-link>
                </div>
              </div>
             </div>
             <div class="search-section-content" v-else ><span class="feedback-request">No users found.</span></div>
            <div class="search-nav">

            </div>
      </div>
      </div>
    </div>
</template>

<script>
import axios from "axios";
import { mapGetters } from 'vuex';

export default {
  name: 'SearchPage',
  data() {
    return {
      users: []
    };
  },
  computed: {
    ...mapGetters(['getUserId'])
  },
  created() {
    this.fetchUsers();
  },
  methods: {
    async fetchUsers() {
      try {
        const response = await axios.get('http://localhost:8085/api/v1/user/allusers');
        const allUsers = response.data;


        console.log('Current user ID:', this.getUserId);


        this.users = allUsers.filter(user => user.userid !== parseInt(this.getUserId));

      } catch (error) {
        console.error("There was an error fetching the users:", error);
      }
    }
  }
}
</script>

<style scoped>
/* Add any custom styles here */
</style>

<template>
  <header class="navbar-element">
    <div class="nav-header user-login">
      <div class="user-img"></div>


      <router-link v-if="!username" to="/login">
        <div class="user-auth">Login</div>
      </router-link>


      <div v-else>
        <div class="user-auth">Welcome, {{ username }}</div>
        <button @click="logout" class="logout-btn">Logout</button>
      </div>
    </div>
    <nav class="navbar">
      <div class="navbar-head-title" v-if="username"><span>Chat</span></div>
      <div class="links" v-if="username">
        <ul>
          <li>
            <router-link to="/search"><i class="fa fa-users"></i> Find people</router-link>
          </li>
          <li>
            <router-link to="/group-chat"><i class="fa fa-comments"></i> Create Group</router-link>
          </li>
        </ul>
      </div>
    </nav>
  </header>
</template>

<script>
import { computed } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';

export default {
  name: "NavbarComponent",
  setup() {
    const store = useStore();
    const router = useRouter();
    const username = computed(() => store.state.username);


    const logout = () => {
      store.dispatch('logoutUser');
      router.push('/login');
    };

    return { username, logout };
  }
};
</script>
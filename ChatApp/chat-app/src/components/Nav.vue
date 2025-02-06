<template>
  <header class="main-navbar-element">
    <div class="mini-navbar-element" v-if="username">
      <div class="mini-nav-item"><router-link to="/find-people"><i class="fa fa-search" aria-hidden="true"></i></router-link></div>
      <div class="mini-nav-item"><router-link to="/new-chat"><i class="fa fa-plus" aria-hidden="true"></i></router-link></div>
      <div class="mini-nav-item"><router-link to="/search"><i class="fa fa-cog" aria-hidden="true"></i></router-link></div>
    </div>
    <div class="navbar-element">
      <div :class="['nav-header', 'user-login', { 'header-borderless': !username }]">
      <div class="user-img"></div>


      <router-link v-if="!username" to="/login">
        <div class="user-auth">Login</div>
      </router-link>


      <div v-else>
        <div class="user-auth">Welcome, {{ username }}</div>
      </div>
    </div>
    <nav class="navbar" v-if="username">
      <div class="navbar-head-title"><span>Chats</span></div>

    </nav>
    <div class="user-logout" v-if="username">
      <button @click="logout" class="logout-btn">Logout</button>
    </div>
    </div>
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
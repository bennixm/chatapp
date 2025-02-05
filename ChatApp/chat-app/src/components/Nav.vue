<template>
  <header class="navbar-element">
    <div class="nav-header user-login">
      <div class="user-img"></div>
      <router-link v-if="!username" to="/login">
        <div class="user-auth">Login</div>
      </router-link>
      <div v-else class="user-auth">Welcome, {{ username }}</div>
    </div>
    <nav class="navbar">
      <div class="navbar-head-title"><span>Chat</span></div>
      <div class="links">
        <ul>
          <li><router-link to="/search"><i class="fa fa-users"></i> Find people</router-link></li>
          <li><router-link to="/group-chat"><i class="fa fa-comments"></i> Create Group</router-link></li>
        </ul>
      </div>
    </nav>
  </header>
</template>

<script>
import { computed } from 'vue';
import { useStore } from 'vuex';

export default {
  name: "NavbarComponent",
  setup() {
    const store = useStore();
    const username = computed(() => store.state.username);

    return { username };
  }
};
</script>

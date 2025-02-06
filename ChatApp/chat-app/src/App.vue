<template>
  <NavComponent/>
  <div :class="['content', { 'content-logout': !username }]">
  <router-view />
 </div>
</template>

<script>
import NavComponent from './components/Nav.vue';
import { computed } from 'vue';
import { useStore } from 'vuex';


export default {
  name: 'App',
  components: {
    NavComponent
  },
  setup() {
    const store = useStore();
    const username = computed(() => store.state.username);


    return { username };
  }
}

</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>

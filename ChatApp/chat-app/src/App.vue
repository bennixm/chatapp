<template>
  <NavComponent/>
  <div class="content">
  <router-view />
 </div>
</template>

<script>
import NavComponent from './components/Nav.vue';

export default {
  name: 'App',
  components: {
    NavComponent
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

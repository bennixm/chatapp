<template>
  <NavComponent/>
  <router-view />
  <FooterComponent/>
</template>

<script>
import NavComponent from './components/Nav.vue'
import FooterComponent from './components/Footer.vue'

export default {
  name: 'App',
  components: {
    NavComponent,
    FooterComponent
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

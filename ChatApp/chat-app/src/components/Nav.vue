<template>
    <header class="navbar-element">
       <nav class="navbar">
        <div class="logo">
          <div class="logo-container">
            <div class="logo-img"></div>
          </div>
        </div>
        <div class="links">
          <ul>
              <li><router-link to="/">Home</router-link></li>
              <li><router-link to="/chat">Chat</router-link></li>
          </ul>
        </div>
       </nav>
  </header>
</template>



<script>

export default {
  name: "NavbarComponent",
};
</script>

<style>

</style>

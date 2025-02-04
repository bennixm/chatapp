<template>
    <header class="navbar-element">
      <div class="nav-header user-login">
        <div class="user-img"></div>
        <router-link to="/login"><div class="user-auth"> Login</div></router-link>
      </div>
       <nav class="navbar">
        <div class="navbar-head-title"><span>Pages</span></div>
        <div class="links">
          <ul>
              <li><router-link to="/"><i class="fa fa-home" aria-hidden="true"></i> Home</router-link></li>
              <li><router-link to="/chatapp"><i class="fa fa-comment" aria-hidden="true"></i> Chat</router-link></li>
          </ul>
        </div>
        <div class="navbar-head-title"><span>Chat</span></div>
        <div class="links">
          <ul>
              <li><router-link to="/search"><i class="fa fa-users" aria-hidden="true"></i> Find people</router-link></li>
              <li><router-link to="/group-chat"><i class="fa fa-comments" aria-hidden="true"></i> Create Group</router-link></li>
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

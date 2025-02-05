import { createRouter, createWebHistory } from 'vue-router';
import { useStore } from 'vuex';
import HomePage from '@/views/HomePage.vue';
import ChatPage from '@/views/ChatPage.vue';
import RegisterPage from '@/views/Register.vue';
import LoginPage from '@/views/Login.vue';

const routes = [
  { path: '/', name: 'HomePage', component: HomePage },
  {
    path: '/chatapp',
    name: 'ChatPage',
    component: ChatPage,
    beforeEnter: (to, from, next) => {
      const store = useStore();

      if (!store.state.username) {
        next('/login');
      } else {
        next();
      }
    },
  },
  {
    path: '/register',
    name: 'RegisterPage',
    component: RegisterPage,
    beforeEnter: (to, from, next) => {
      const store = useStore();
      if (store.state.username) {
        next('/');
      } else {
        next();
      }
    },
  },
  {
    path: '/login',
    name: 'LoginPage',
    component: LoginPage,
    beforeEnter: (to, from, next) => {
      const store = useStore();
      if (store.state.username) {
        next('/');
      } else {
        next();
      }
    },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;

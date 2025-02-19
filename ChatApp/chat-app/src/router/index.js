import { createRouter, createWebHistory } from 'vue-router';
import { useStore } from 'vuex';
import HomePage from '@/views/HomePage.vue';
import ChatPage from '@/views/ChatPage.vue';
import DashboardPage from '@/views/Dashboard.vue';
import RegisterPage from '@/views/Register.vue';
import LoginPage from '@/views/Login.vue';
import SettingsPage from '@/views/Settings.vue';
import SearchPage from '@/views/Search.vue';
import NewChat from '@/views/NewChat.vue';
import StatsPage from '@/views/StatsPage.vue';

const routes = [
  { path: '/', name: 'HomePage', component: HomePage },
  {
    path: '/chatapp',
    name: 'DashboardPage',
    component: DashboardPage,
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
    path: '/find-people',
    name: 'SearchPage',
    component: SearchPage,
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
    path: '/stats',
    name: 'Stats',
    component: StatsPage,
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
    path: '/chat/:receiverId',  // Add receiverUsername to the path
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
    path: '/new-chat',
    name: 'NewChat',
    component: NewChat,
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
    path: '/settings',
    name: 'SettingsPage',
    component: SettingsPage,
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

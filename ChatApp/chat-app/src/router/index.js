import { createRouter, createWebHistory } from 'vue-router';
import { useStore } from 'vuex';
import { isAuthenticated } from '../utils/auth';

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
  { path: '/chatapp', name: 'DashboardPage', component: DashboardPage, meta: { requiresAuth: true }},
  { path: '/find-people', name: 'SearchPage', component: SearchPage, meta: { requiresAuth: true }},
  { path: '/stats', name: 'Stats', component: StatsPage, meta: { requiresAuth: true }},
  { path: '/chat/:receiverId', name: 'ChatPage', component: ChatPage, meta: { requiresAuth: true }},
  { path: '/new-chat', name: 'NewChat', component: NewChat, meta: { requiresAuth: true }},
  { path: '/settings', name: 'SettingsPage', component: SettingsPage, meta: { requiresAuth: true }},
  { path: '/register', name: 'RegisterPage', component: RegisterPage },
  { path: '/login', name: 'LoginPage', component: LoginPage },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach(async (to, from, next) => {
  const store = useStore();
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);

  if (requiresAuth) {
    const valid = await isAuthenticated();
    if (!valid) {
      store.dispatch('logoutUser');
      return next('/login');
    }
  }

  if ((to.path === '/login' || to.path === '/register') && await isAuthenticated()) {
    return next('/');
  }

  next();
});

export default router;

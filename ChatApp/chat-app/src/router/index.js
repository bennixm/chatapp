import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '@/views/HomePage.vue';
import ChatPage from '@/views/ChatPage.vue';
import RegisterPage from '@/views/Register.vue';
import LoginPage from '@/views/Login.vue';

const routes = [
  { path: '/', component: HomePage },
  { path: '/chatapp', component: ChatPage },
  { path: '/register', component: RegisterPage },
  { path: '/login', component: LoginPage }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;

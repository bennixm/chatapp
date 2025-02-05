import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '@/views/HomePage.vue';
import ChatPage from '@/views/ChatPage.vue';
import RegisterPage from '@/views/Register.vue';
import LoginPage from '@/views/Login.vue';

const routes = [
  { path: '/', name: 'HomePage', component: HomePage },
  { path: '/chatapp', name: 'ChatPage', component: ChatPage },
  { path: '/register', name: 'RegisterPage', component: RegisterPage },
  { path: '/login', name: 'LoginPage', component: LoginPage }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;

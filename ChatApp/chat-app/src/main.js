import { createApp } from 'vue'
import App from './App.vue';
import router from './router';
import './assets/css/style.css';
import './assets/font-awesome/css/font-awesome.min.css';

createApp(App).use(router).mount('#app');


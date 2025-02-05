import { createApp } from 'vue'
import App from './App.vue';
import router from './router';
import store from './store/store';
import './assets/css/style.css';
import './assets/font-awesome/css/font-awesome.min.css';

createApp(App)
    .use(router)
    .use(store)
    .mount('#app');


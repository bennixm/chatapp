import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store/store'
import './assets/css/style.css'
import './assets/font-awesome/css/font-awesome.min.css'
import "vue-data-ui/style.css"

import { VueUiRadar } from 'vue-data-ui'

const app = createApp(App)

app.component("VueUiRadar", VueUiRadar)

app.use(router)
app.use(store)
app.mount('#app')

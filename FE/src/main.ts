import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { i18n } from './i18n'
import { notificationService } from './services/notificationService'
import './assets/styles/main.scss'
import Toast from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'

const pinia = createPinia()
const app = createApp(App)

// Register service worker for push notifications
if ('serviceWorker' in navigator) {
  notificationService.registerServiceWorker()
    .then(registration => {
      console.log('Service worker was registered.')
      console.log({ serviceWorkerRegistration: registration })
    })
    .catch(error => {
      console.error('Service worker registration failed:', error)
    })
}

app.use(pinia)
app.use(router)
app.use(i18n)
app.use(Toast, {
    position: 'bottom-right',
    timeout: 5000,
    closeOnClick: true,
    pauseOnFocusLoss: true,
    pauseOnHover: true,
    draggable: true,
    draggablePercent: 0.6,
})
app.mount('#app')

<script setup lang="ts">
import { onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from './stores/auth'
import AppHeader from './components/layout/AppHeader.vue'
import AppFooter from './components/layout/AppFooter.vue'
import LoadingOverlay from './components/ui/LoadingOverlay.vue'

const router = useRouter()
const authStore = useAuthStore()

const isLoading = computed(() => authStore.isInitializing)
const isLoggedIn = computed(() => authStore.isAuthenticated)

onMounted(async () => {
  await authStore.initializeAuth()
})
</script>

<template>
  <div class="app-container flex flex-col min-h-screen">
    <LoadingOverlay v-if="isLoading" />
    
    <AppHeader v-if="!isLoading" />
    
    <main class="flex-grow">
      <router-view v-slot="{ Component }">
        <transition name="page" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
    
    <AppFooter v-if="!isLoading" />
  </div>
</template>

<style scoped>
.app-container {
  position: relative;
}
</style>
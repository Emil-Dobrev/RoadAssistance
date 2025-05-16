<template>
  <header class="bg-white shadow">
    <nav class="container mx-auto px-4 py-3">
      <div class="flex flex-wrap items-center justify-between">
        <!-- Logo + Mobile Toggle -->
        <div class="flex items-center space-x-4">
          <button
              @click="toggleMenu"
              class="md:hidden text-xl p-2"
              aria-label="Toggle menu"
          >
            <i class="fas fa-bars"></i>
          </button>
          <router-link
              to="/"
              class="text-lg font-semibold text-blue-600 hover:text-blue-800 transition-colors"
          >
            RoadAssist
          </router-link>
        </div>

        <!-- Desktop Navigation -->
        <div class="hidden md:flex flex-grow justify-center space-x-4 mt-3 md:mt-0">
          <template v-if="isLoggedIn">
            <template v-if="userRole === UserRole.USER">
              <NavLink
                  v-for="link in userNavLinks"
                  :key="link.path"
                  :to="link.path"
                  :icon="link.icon"
                  @click="closeMenus"
              >
                {{ link.name }}
              </NavLink>
            </template>
            <template v-else-if="userRole === UserRole.SERVICE_PROVIDER">
              <NavLink
                  v-for="link in providerNavLinks"
                  :key="link.path"
                  :to="link.path"
                  :icon="link.icon"
                  @click="closeMenus"
              >
                {{ link.name }}
              </NavLink>
            </template>
          </template>
        </div>

        <!-- Right-side Buttons -->
        <div class="flex items-center space-x-4 mt-3 md:mt-0">
          <Button @click="toggleLanguage" variant="text">
            {{ locale === 'bg' ? 'EN' : 'BG' }}
          </Button>

          <template v-if="isLoggedIn">
            <div class="relative">
              <button
                  @click="toggleUserMenu"
                  class="flex items-center space-x-2"
              >
                <span>{{ userName }}</span>
                <i class="fas fa-chevron-down"></i>
              </button>

              <div
                  v-if="isUserMenuOpen"
                  class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-50"
              >
                <router-link
                    :to="profilePath"
                    class="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                >
                  Profile
                </router-link>
                <button
                    @click="handleLogout"
                    class="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                >
                  {{ t('auth.logout') }}
                </button>
              </div>
            </div>
          </template>
          <template v-else>
            <NavLink to="/auth/login">{{ t('auth.login') }}</NavLink>
            <NavLink to="/auth/register">{{ t('auth.register') }}</NavLink>
          </template>
        </div>

        <!-- Mobile Menu -->
        <div
            v-if="isMenuOpen"
            class="w-full mt-4 md:hidden"
        >
          <template v-if="isLoggedIn">
            <template v-if="userRole === UserRole.USER">
              <NavLink
                  v-for="link in userNavLinks"
                  :key="link.path"
                  :to="link.path"
                  class="block py-2"
                  @click="closeMenus"
              >
                {{ link.name }}
              </NavLink>
            </template>
            <template v-else-if="userRole === UserRole.SERVICE_PROVIDER">
              <NavLink
                  v-for="link in providerNavLinks"
                  :key="link.path"
                  :to="link.path"
                  class="block py-2"
                  @click="closeMenus"
              >
                {{ link.name }}
              </NavLink>
            </template>
          </template>
        </div>
      </div>
    </nav>
  </header>
</template>


<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import { useI18n } from 'vue-i18n'
import NavLink from '../ui/NavLink.vue'
import Button from '../ui/Button.vue'
import { UserRole } from "../../types"

const router = useRouter()
const authStore = useAuthStore()
const { t, locale } = useI18n()

const isMenuOpen = ref(false)
const isUserMenuOpen = ref(false)

const isLoggedIn = computed(() => authStore.isAuthenticated)
const userRole = computed(() => {
      if(authStore.user !=  null) {
        return  authStore.user?.role
      } else {
      return authStore.serviceProvider?.role
      }
})

const userName = computed(() =>{
  if(authStore.user !=  null) {
   return  authStore.user ? authStore.user.fullName : ''
  } else {
    return authStore.serviceProvider ? authStore.serviceProvider.fullName : ''
  }
})

const profilePath = computed(() => {
  // TODO add id for profile
  if (userRole.value === UserRole.USER) {
    return '/user/profile'
  } else if (userRole.value === UserRole.SERVICE_PROVIDER) {
    return `/provider/profile/${authStore.serviceProvider?.id}`
  }
  return '/'
})

const toggleLanguage = () => {
  locale.value = locale.value === 'bg' ? 'en' : 'bg'
}

const userNavLinks = computed(() => [
  { name: t('navigation.home'), path: '/', icon: 'home' },
  { name: t('navigation.requests'), path: '/user/request', icon: 'plus-circle' },
  { name: t('navigation.history'), path: '/user/history', icon: 'clock' }
])

const providerNavLinks = computed(() => [
  { name: t('navigation.home'), path: '/provider', icon: 'home' },
  { name: t('navigation.requests'), path: '/provider/requests', icon: 'bell' },
  { name: t('navigation.services'), path: '/provider/services', icon: 'wrench' }
])

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value
  if (isMenuOpen.value) {
    isUserMenuOpen.value = false
  }
}

const toggleUserMenu = () => {
  isUserMenuOpen.value = !isUserMenuOpen.value
  if (isUserMenuOpen.value) {
    isMenuOpen.value = false
  }
}

const handleLogout = () => {
  authStore.logout()
  isUserMenuOpen.value = false
  router.push({ name: 'Home' })
}

const closeMenus = () => {
  isMenuOpen.value = false
  isUserMenuOpen.value = false
}
</script>
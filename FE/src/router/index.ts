import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import Home from '../views/Home.vue'
import NotFound from '../views/NotFound.vue'
import {UserRole} from "../types";

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: { requiresAuth: false }
  },
  {
    path: '/auth',
    name: 'Auth',
    component: () => import('../views/Auth.vue'),
    meta: { requiresAuth: false, hideForAuth: true },
    children: [
      {
        path: 'login',
        name: 'Login',
        component: () => import('../views/auth/Login.vue')
      },
      {
        path: 'register',
        name: 'Register',
        component: () => import('../views/auth/Register.vue')
      },
      {
        path: 'forgot-password',
        name: 'ForgotPassword',
        component: () => import('../views/auth/ForgotPassword.vue')
      }
    ]
  },
  {
    path: '/user',
    name: 'UserDashboard',
    component: () => import('../views/user/Dashboard.vue'),
    meta: { requiresAuth: true, userType: 'USER' }
  },
  {
    path: '/user/request',
    name: 'RequestAssistance',
    component: () => import('../views/user/RequestAssistance.vue'),
    meta: { requiresAuth: true, userType: 'USER' }
  },
  {
    path: '/user/history',
    name: 'UserHistory',
    component: () => import('../views/user/History.vue'),
    meta: { requiresAuth: true, userType: 'USER' }
  },
  {
    path: '/provider',
    name: 'ProviderDashboard',
    component: () => import('../views/provider/Dashboard.vue'),
    meta: { requiresAuth: true, userType: 'SERVICE_PROVIDER' }
  },
  {
    path: '/provider/requests',
    name: 'ManageRequests',
    component: () => import('../views/provider/Requests.vue'),
    meta: { requiresAuth: true, userType: 'SERVICE_PROVIDER' }
  },
  // {
  //   path: '/provider/services',
  //   name: 'ManageServices',
  //   component: () => import('../views/provider/Services.vue'),
  //   meta: { requiresAuth: true, userType: 'SERVICE_PROVIDER' }
  // },
  {
    path: '/provider/profile/:providerId',
    name: 'ProviderProfile',
    component: () => import('../views/provider/Profile.vue'),
    meta: {  }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: NotFound,
    meta: { requiresAuth: false }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()
  
  // Wait for auth initialization if still loading
  if (authStore.isInitializing) {
    await new Promise<void>(resolve => {
      const unwatch = authStore.$subscribe((mutation, state) => {
        if (!state.isInitializing) {
          unwatch()
          resolve()
        }
      })
    })
  }

  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const userTypeRequired = to.meta.userType
  const hideForAuth = to.matched.some(record => record.meta.hideForAuth)
  
  if (requiresAuth && !authStore.isAuthenticated) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else if (
    requiresAuth && 
    userTypeRequired && 
    authStore.user && 
    authStore.user.role !== userTypeRequired
  ) {
    next({ name: authStore.user.role === UserRole.USER ? 'UserDashboard' : 'ProviderDashboard' })
  } else if (hideForAuth && authStore.isAuthenticated) {
    next({ name: authStore.user?.role === UserRole.USER ? 'UserDashboard' : 'ProviderDashboard' })
  } else {
    next()
  }
})

export default router
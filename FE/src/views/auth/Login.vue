<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import Button from '../../components/ui/Button.vue'
import {Coordinates, UserRole} from "../../types";
import { toast } from 'vue3-toastify'
import {getCurrentLocation} from "../../utils/locationUtils.ts";
import {useUserLocationStore} from "../../stores/userLocation.ts";
import {updateProviderLocation} from "../../services/providerService.ts";


const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const userLocationStore = useUserLocationStore()


const email = ref('')
const password = ref('')
const error = ref('')
const isLoading = ref(false)

const updateLocation = async () => {
  const position = await getCurrentLocation()
      .catch((error) => {
        toast.error('Please enable location in your browser settings.')
      })

  if (position && authStore.serviceProvider) {
    userLocationStore.setLocation(
        position.coords.latitude,
        position.coords.longitude
    )
   await updateProviderLocation(authStore.serviceProvider.id, {
     latitude: position.coords.latitude,
     longitude: position.coords.longitude
   } as Coordinates)
  }
}

const handleSubmit = async () => {
  if (!email.value || !password.value) {
    error.value = 'Please fill in all fields'
    return
  }
  
  isLoading.value = true
  error.value = ''
  
  try {
    const success = await authStore.login({
      email: email.value,
      password: password.value
    })
    if (success) {
      try {
       await updateLocation()
      } catch (err: any) {
        // do nothing
      }
      const redirectPath = route.query.redirect as string || 
        (authStore.user?.role === UserRole.USER ? '/' : '/provider')

      await router.push(redirectPath)
    } else {
      toast.error("Wrong email or password")
    }
  } catch (err: any) {
    toast.error("Wrong email or password")
    error.value = err.message || 'Failed to login'
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div>
    <h2 class="text-center text-3xl font-bold text-neutral-900">Sign in to your account</h2>
    
    <form class="mt-8 space-y-6" @submit.prevent="handleSubmit">
      <div class="space-y-4">
        <div>
          <label for="email" class="form-label">Email address</label>
          <input
            id="email"
            type="email"
            v-model="email"
            required
            class="input"
            placeholder="Enter your email"
          />
        </div>
        
        <div>
          <label for="password" class="form-label">Password</label>
          <input
            id="password"
            type="password"
            v-model="password"
            required
            class="input"
            placeholder="Enter your password"
          />
        </div>
      </div>
      
      <div class="flex items-center justify-between">
        <div class="text-sm">
          <router-link 
            to="/auth/forgot-password" 
            class="text-primary-600 hover:text-primary-500"
          >
            Forgot your password?
          </router-link>
        </div>
      </div>
      
      <div v-if="error" class="text-error-500 text-sm text-center">
        {{ error }}
      </div>
      
      <Button
        type="submit"
        variant="primary"
        :loading="isLoading"
        :disabled="isLoading"
        fullWidth
      >
        Sign in
      </Button>
      
      <div class="text-center">
        <p class="text-sm text-neutral-600">
          Don't have an account?
          <router-link 
            to="/auth/register" 
            class="text-primary-600 hover:text-primary-500 font-medium"
          >
            Sign up
          </router-link>
        </p>
      </div>
    </form>
  </div>
</template>
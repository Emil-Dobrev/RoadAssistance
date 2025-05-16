<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from '../../services/authService'
import Button from '../../components/ui/Button.vue'

const router = useRouter()

const email = ref('')
const error = ref('')
const success = ref(false)
const isLoading = ref(false)

const handleSubmit = async () => {
  if (!email.value) {
    error.value = 'Please enter your email address'
    return
  }
  
  isLoading.value = true
  error.value = ''
  
  try {
    await authService.forgotPassword(email.value)
    success.value = true
  } catch (err: any) {
    error.value = err.message || 'Failed to process request'
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div>
    <h2 class="text-center text-3xl font-bold text-neutral-900">Reset your password</h2>
    <p class="mt-2 text-center text-sm text-neutral-600">
      Enter your email address and we'll send you instructions to reset your password.
    </p>
    
    <div v-if="success" class="mt-8">
      <div class="rounded-md bg-success-50 p-4">
        <div class="flex">
          <div class="flex-shrink-0">
            <svg class="h-5 w-5 text-success-400" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
            </svg>
          </div>
          <div class="ml-3">
            <h3 class="text-sm font-medium text-success-800">Success!</h3>
            <div class="mt-2 text-sm text-success-700">
              <p>
                If an account exists for {{ email }}, you will receive password reset instructions.
              </p>
            </div>
            <div class="mt-4">
              <Button
                variant="primary"
                size="sm"
                @click="router.push('/auth/login')"
              >
                Return to login
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <form v-else class="mt-8 space-y-6" @submit.prevent="handleSubmit">
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
        Send reset instructions
      </Button>
      
      <div class="text-center">
        <router-link 
          to="/auth/login" 
          class="text-primary-600 hover:text-primary-500 text-sm font-medium"
        >
          Return to login
        </router-link>
      </div>
    </form>
  </div>
</template>
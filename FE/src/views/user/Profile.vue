<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { useAuthStore } from '../../stores/auth'
import Button from '../../components/ui/Button.vue'

const { t } = useI18n()
const authStore = useAuthStore()

const isEditing = ref(false)
const isLoading = ref(false)
const error = ref('')
const photoInput = ref<HTMLInputElement | null>(null)

const form = ref({
  firstName: authStore.user?.firstName || '',
  lastName: authStore.user?.lastName || '',
  email: authStore.user?.email || '',
  phoneNumber: authStore.user?.phoneNumber || ''
})

const handlePhotoChange = async (event: Event) => {
  const input = event.target as HTMLInputElement
  if (input.files && input.files[0]) {
    const file = input.files[0]
    // Handle photo upload logic here
  }
}

const handleSubmit = async () => {
  isLoading.value = true
  error.value = ''
  
  try {
    // Update profile logic here
    isEditing.value = false
  } catch (err: any) {
    error.value = err.message
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  // Load user details if needed
})
</script>

<template>
  <div class="min-h-screen bg-neutral-50 py-8 px-4">
    <div class="max-w-3xl mx-auto">
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <!-- Profile Header -->
        <div class="bg-primary-600 text-white p-6">
          <div class="flex items-center space-x-4">
            <div class="relative">
              <div class="w-24 h-24 bg-white rounded-full flex items-center justify-center text-primary-600 text-3xl font-bold overflow-hidden">
                <img 
                  v-if="authStore.user?.avatarUrl" 
                  :src="authStore.user.avatarUrl" 
                  :alt="form.firstName"
                  class="w-full h-full object-cover"
                >
                <span v-else>{{ form.firstName.charAt(0) }}</span>
              </div>
              <button 
                @click="() => photoInput?.click()"
                class="absolute bottom-0 right-0 bg-white rounded-full p-2 shadow-md hover:bg-neutral-100"
              >
                <span class="sr-only">{{ t('profile.editPhoto') }}</span>
                <i class="fas fa-camera text-primary-600"></i>
              </button>
              <input
                ref="photoInput"
                type="file"
                accept="image/*"
                class="hidden"
                @change="handlePhotoChange"
              >
            </div>
            <div>
              <h1 class="text-2xl font-bold">{{ form.firstName }} {{ form.lastName }}</h1>
              <p class="text-primary-100">{{ form.email }}</p>
            </div>
          </div>
        </div>

        <!-- Profile Form -->
        <div class="p-6">
          <form @submit.prevent="handleSubmit" class="space-y-6">
            <div v-if="error" class="bg-error-50 text-error-700 p-4 rounded-md">
              {{ error }}
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div class="form-group">
                <label for="firstName" class="form-label">{{ t('auth.firstName') }}</label>
                <input
                  id="firstName"
                  v-model="form.firstName"
                  type="text"
                  :disabled="!isEditing"
                  class="input"
                  required
                />
              </div>

              <div class="form-group">
                <label for="lastName" class="form-label">{{ t('auth.lastName') }}</label>
                <input
                  id="lastName"
                  v-model="form.lastName"
                  type="text"
                  :disabled="!isEditing"
                  class="input"
                  required
                />
              </div>

              <div class="form-group">
                <label for="email" class="form-label">{{ t('auth.email') }}</label>
                <input
                  id="email"
                  v-model="form.email"
                  type="email"
                  :disabled="!isEditing"
                  class="input"
                  required
                />
              </div>

              <div class="form-group">
                <label for="phone" class="form-label">{{ t('auth.phone') }}</label>
                <input
                  id="phone"
                  v-model="form.phoneNumber"
                  type="tel"
                  :disabled="!isEditing"
                  class="input"
                />
              </div>
            </div>

            <div class="flex justify-end space-x-4">
              <Button
                v-if="!isEditing"
                type="button"
                variant="secondary"
                @click="isEditing = true"
              >
                {{ t('common.edit') }}
              </Button>
              <Button
                v-else
                type="submit"
                variant="primary"
                :loading="isLoading"
                :disabled="isLoading"
              >
                {{ t('profile.updateProfile') }}
              </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>
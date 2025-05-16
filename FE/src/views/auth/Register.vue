<script setup lang="ts">
import {computed, onMounted, ref} from 'vue'
import {useRouter} from 'vue-router'
import {useAuthStore} from '../../stores/auth'
import {UserRole} from '../../types'
import Button from '../../components/ui/Button.vue'
import {useUserLocationStore} from "../../stores/userLocation.ts";
import {getCurrentLocation} from "../../utils/locationUtils.ts";
import {toast} from "vue3-toastify";
import {enablePushNotifications, notificationService,} from "../../services/notificationService.ts";


const router = useRouter()
const authStore = useAuthStore()
const userLocationStore = useUserLocationStore()

const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const firstName = ref('')
const lastName = ref('')
const phone = ref('')
const role = ref<UserRole>(UserRole.USER)
const error = ref('')
const isLoading = ref(false)
const companyName = ref('')
const companyDescription = ref('')
const notificationEnabled = ref(false)
const provider = authStore.serviceProvider

onMounted(async () => {
  // notification
  // await enablePushNotifications()
  await getCurrentLocation()
      .catch((error) => {
        toast.error('Please enable location in your browser settings.')
          }
      )
})

const enableNotifications = () => {
  // enablePushNotifications()
  if(notificationEnabled.value === true){
    notificationEnabled.value = false
  } else {
    enablePushNotifications()
    notificationEnabled.value = true
  }

}

const handleSubmit = async () => {
  if (!email.value || !password.value || !firstName.value || !lastName.value) {
    error.value = 'Please fill in all required fields'
    return
  }

  if (password.value !== confirmPassword.value) {
    error.value = 'Passwords do not match'
    return
  }

  const location = computed(() => userLocationStore.location)
  isLoading.value = true
  error.value = ''

  try {
    if(role.value === UserRole.SERVICE_PROVIDER && location.value?.latitude === undefined || location.value?.latitude === null) {
      await getCurrentLocation()
          .catch((error) => {
            toast.error('Please enable location in your browser settings.')
              }
          )
    }
    const success = await authStore.register({
      email: email.value,
      password: password.value,
      firstName: firstName.value,
      lastName: lastName.value,
      phoneNumber: phone.value,
      companyName: companyName.value,
      companyDescription: companyDescription.value,
      role: role.value,
      location: location.value ?? undefined
    })

    if (success) {
      if(role.value === UserRole.SERVICE_PROVIDER && notificationEnabled.value ) {
        const id = authStore.serviceProvider?.id
        // send subscription
        if(id){
          try {
            await notificationService.subscribeUserToPush(id)
          } catch (e) {
            // do nothing
          }

        }
      }
      router.push(role.value === UserRole.USER ? '/user' : '/provider')
    }
  } catch (err: any) {
    error.value = err.message || 'Failed to register'
  } finally {
    isLoading.value = false
  }
}
</script>
<template>
  <div>
    <h2 class="text-center text-3xl font-bold text-neutral-900">Create your account</h2>

    <form class="mt-8 space-y-6" @submit.prevent="handleSubmit">
      <div class="space-y-4">
        <div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
          <div>
            <label for="firstName" class="form-label">First name</label>
            <input
                id="firstName"
                type="text"
                v-model="firstName"
                required
                class="input"
                placeholder="Enter your first name"
            />
          </div>

          <div>
            <label for="lastName" class="form-label">Last name</label>
            <input
                id="lastName"
                type="text"
                v-model="lastName"
                required
                class="input"
                placeholder="Enter your last name"
            />
          </div>
        </div>

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
          <label for="phone" class="form-label">Phone number</label>
          <input
              id="phone"
              type="tel"
              v-model="phone"
              class="input"
              placeholder="Enter your phone number"
          />
        </div>

        <div v-if="role === UserRole.SERVICE_PROVIDER" class="flex items-center space-x-3">
          <!-- Button with Bell and optional Checkmark -->
          <button
              @click="enableNotifications()"
              class="flex items-center px-4 py-2 rounded-2xl border shadow-sm transition-colors duration-200"
              :class="{
            'bg-green-500 text-white': notificationEnabled,
            'bg-white text-gray-700 border-gray-300': !notificationEnabled
          }"
          >
            <!-- Bell icon -->
            <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round"
                    d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V4a2 2 0 10-4 0v1.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0a3 3 0 01-6 0h6z" />
            </svg>
            <span>{{ notificationEnabled ? 'Push Enabled' : 'Enable Push' }}</span>

            <!-- Checkmark when enabled -->
            <svg v-if="notificationEnabled" class="w-5 h-5 ml-2 text-white" fill="none" stroke="currentColor" stroke-width="2"
                 viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7" />
            </svg>
          </button>

          <!-- Tooltip with Info icon -->
          <div class="relative group cursor-pointer">
            <!-- Info icon -->
            <svg class="w-5 h-5 text-gray-500" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round"
                    d="M13 16h-1v-4h-1m1-4h.01M12 20a8 8 0 100-16 8 8 0 000 16z" />
            </svg>

            <!-- Tooltip content -->
            <div
                class="absolute left-6 top-0 w-72 p-3 text-sm text-white bg-gray-800 rounded shadow-lg opacity-0 group-hover:opacity-100 transition-opacity z-10"
            >
              If this is selected, you'll receive a notification when service providers near you are available during your
              search.
            </div>
          </div>
        </div>

        <!-- Only display company name and description if service provider is selected -->
        <div v-if="role === UserRole.SERVICE_PROVIDER">
          <div>
            <label for="companyName" class="form-label">Company name</label>
            <input
                id="companyName"
                type="text"
                v-model="companyName"
                required
                class="input"
                placeholder="Company name"
            />
          </div>

          <div>
            <label for="companyDescription" class="form-label">Company Description</label>
            <input
                id="companyDescription"
                type="text"
                v-model="companyDescription"
                class="input"
                placeholder="Company description"
            />
          </div>
        </div>

        <div>
          <label for="password" class="form-label">Password</label>
          <input
              id="password"
              type="password"
              v-model="password"
              required
              class="input"
              placeholder="Create a password"
          />
        </div>

        <div>
          <label for="confirmPassword" class="form-label">Confirm password</label>
          <input
              id="confirmPassword"
              type="password"
              v-model="confirmPassword"
              required
              class="input"
              placeholder="Confirm your password"
          />
        </div>

        <div>
          <label class="form-label">Account type</label>
          <div class="mt-2 grid grid-cols-2 gap-4">
            <button
                type="button"
                :class="[
                'p-4 border rounded-lg text-left',
                role === UserRole.USER
                  ? 'border-primary-500 bg-primary-50 text-primary-700'
                  : 'border-neutral-300 hover:border-primary-300'
              ]"
                @click="role = UserRole.USER"
            >
              <div class="font-medium">User</div>
              <div class="text-sm text-neutral-600">Need road assistance</div>
            </button>

            <button
                type="button"
                :class="[
                'p-4 border rounded-lg text-left',
                role === UserRole.SERVICE_PROVIDER
                  ? 'border-primary-500 bg-primary-50 text-primary-700'
                  : 'border-neutral-300 hover:border-primary-300'
              ]"
                @click="role = UserRole.SERVICE_PROVIDER"
            >
              <div class="font-medium">Service Provider</div>
              <div class="text-sm text-neutral-600">Provide assistance</div>
            </button>
          </div>
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
        Create account
      </Button>

      <div class="text-center">
        <p class="text-sm text-neutral-600">
          Already have an account?
          <router-link
              to="/auth/login"
              class="text-primary-600 hover:text-primary-500 font-medium"
          >
            Sign in
          </router-link>
        </p>
      </div>
    </form>
  </div>
</template>

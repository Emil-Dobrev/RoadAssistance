<!--<script setup lang="ts">-->
<!--import { ref, onMounted } from 'vue'-->
<!--import { useI18n } from 'vue-i18n'-->
<!--import { useAuthStore } from '../../stores/auth'-->
<!--import Button from '../ui/Button.vue'-->

<!--const { t } = useI18n()-->
<!--const authStore = useAuthStore()-->

<!--const form = ref({-->
<!--  firstName: '',-->
<!--  lastName: '',-->
<!--  email: '',-->
<!--  password: '',-->
<!--  confirmPassword: '',-->
<!--  companyName: '',-->
<!--  phoneNumber: '',-->
<!--  servicesOffered: [] as string[],-->
<!--  address: '',-->
<!--  certification: [] as string[],-->
<!--  location: null as GeolocationCoordinates | null-->
<!--})-->

<!--const error = ref('')-->
<!--const isLoading = ref(false)-->

<!--const availableServices = [-->
<!--  'towing',-->
<!--  'flatTire',-->
<!--  'jumpStart',-->
<!--  'fuelDelivery',-->
<!--  'lockout',-->
<!--  'other'-->
<!--]-->

<!--onMounted(async () => {-->
<!--  try {-->
<!--    const position = await new Promise<GeolocationPosition>((resolve, reject) => {-->
<!--      navigator.geolocation.getCurrentPosition(resolve, reject, {-->
<!--        enableHighAccuracy: true,-->
<!--        timeout: 5000,-->
<!--        maximumAge: 0-->
<!--      })-->
<!--    })-->
<!--    form.value.location = position.coords-->
<!--    console.log(form.value)-->
<!--  } catch (err) {-->
<!--    error.value = t('auth.locationRequired')-->
<!--  }-->
<!--})-->

<!--const handleSubmit = async () => {-->
<!--  if (!form.value.location) {-->
<!--    error.value = t('auth.locationRequired')-->
<!--    return-->
<!--  }-->

<!--  isLoading.value = true-->
<!--  error.value = ''-->

<!--  // try {-->
<!--  //   await authStore.registerProvider({-->
<!--  //     ...form.value,-->
<!--  //     role: 'SERVICE_PROVIDER'-->
<!--  //   })-->
<!--  // } catch (err: any) {-->
<!--  //   error.value = err.message-->
<!--  // } finally {-->
<!--  //   isLoading.value = false-->
<!--  // }-->
<!--}-->
<!--</script>-->

<!--<template>-->
<!--  <form @submit.prevent="handleSubmit" class="space-y-6">-->
<!--    <div class="space-y-4">-->
<!--      <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">-->
<!--        <div class="form-group">-->
<!--          <label for="firstName" class="form-label">{{ t('auth.firstName') }}</label>-->
<!--          <input-->
<!--            id="firstName"-->
<!--            v-model="form.firstName"-->
<!--            type="text"-->
<!--            required-->
<!--            class="input"-->
<!--          />-->
<!--        </div>-->

<!--        <div class="form-group">-->
<!--          <label for="lastName" class="form-label">{{ t('auth.lastName') }}</label>-->
<!--          <input-->
<!--            id="lastName"-->
<!--            v-model="form.lastName"-->
<!--            type="text"-->
<!--            required-->
<!--            class="input"-->
<!--          />-->
<!--        </div>-->
<!--      </div>-->

<!--      <div class="form-group">-->
<!--        <label for="companyName" class="form-label">{{ t('auth.companyName') }}</label>-->
<!--        <input-->
<!--          id="companyName"-->
<!--          v-model="form.companyName"-->
<!--          type="text"-->
<!--          required-->
<!--          class="input"-->
<!--        />-->
<!--      </div>-->

<!--      <div class="form-group">-->
<!--        <label for="email" class="form-label">{{ t('auth.email') }}</label>-->
<!--        <input-->
<!--          id="email"-->
<!--          v-model="form.email"-->
<!--          type="email"-->
<!--          required-->
<!--          class="input"-->
<!--        />-->
<!--      </div>-->

<!--      <div class="form-group">-->
<!--        <label for="phone" class="form-label">{{ t('auth.phone') }}</label>-->
<!--        <input-->
<!--          id="phone"-->
<!--          v-model="form.phoneNumber"-->
<!--          type="tel"-->
<!--          required-->
<!--          class="input"-->
<!--        />-->
<!--      </div>-->

<!--      <div class="form-group">-->
<!--        <label class="form-label">{{ t('auth.servicesOffered') }}</label>-->
<!--        <div class="grid grid-cols-2 gap-2">-->
<!--          <label-->
<!--            v-for="service in availableServices"-->
<!--            :key="service"-->
<!--            class="flex items-center space-x-2 p-2 border rounded hover:bg-gray-50"-->
<!--          >-->
<!--            <input-->
<!--              type="checkbox"-->
<!--              v-model="form.servicesOffered"-->
<!--              :value="service"-->
<!--              class="form-checkbox"-->
<!--            />-->
<!--            <span>{{ t(`services.${service}`) }}</span>-->
<!--          </label>-->
<!--        </div>-->
<!--      </div>-->

<!--      <div class="form-group">-->
<!--        <label for="address" class="form-label">{{ t('auth.address') }}</label>-->
<!--        <textarea-->
<!--          id="address"-->
<!--          v-model="form.address"-->
<!--          required-->
<!--          class="input"-->
<!--          rows="2"-->
<!--        ></textarea>-->
<!--      </div>-->

<!--      <div class="form-group">-->
<!--        <label for="password" class="form-label">{{ t('auth.password') }}</label>-->
<!--        <input-->
<!--          id="password"-->
<!--          v-model="form.password"-->
<!--          type="password"-->
<!--          required-->
<!--          class="input"-->
<!--        />-->
<!--      </div>-->

<!--      <div class="form-group">-->
<!--        <label for="confirmPassword" class="form-label">{{ t('auth.confirmPassword') }}</label>-->
<!--        <input-->
<!--          id="confirmPassword"-->
<!--          v-model="form.confirmPassword"-->
<!--          type="password"-->
<!--          required-->
<!--          class="input"-->
<!--        />-->
<!--      </div>-->
<!--    </div>-->

<!--    <div v-if="error" class="text-error-500 text-sm text-center">-->
<!--      {{ error }}-->
<!--    </div>-->

<!--    <Button-->
<!--      type="submit"-->
<!--      variant="primary"-->
<!--      :loading="isLoading"-->
<!--      :disabled="isLoading"-->
<!--      fullWidth-->
<!--    >-->
<!--      {{ t('auth.register') }}-->
<!--    </Button>-->
<!--  </form>-->
<!--</template>-->
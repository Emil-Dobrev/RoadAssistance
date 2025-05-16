<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAssistanceRequestsStore } from '../../stores/assistanceRequests'
import { useProvidersStore } from '../../stores/providers'
import Button from '../../components/ui/Button.vue'

const assistanceStore = useAssistanceRequestsStore()
const providersStore = useProvidersStore()

const isLoading = ref(false)
const error = ref<string | null>(null)

const vehicleInfo = ref({
  make: '',
  model: '',
  year: '',
  color: '',
  licensePlate: ''
})

const issue = ref('')
const notes = ref('')

const userLocation = ref({
  latitude: 0,
  longitude: 0
})

onMounted(async () => {
  try {
    // Get user's current location
    if ('geolocation' in navigator) {
      const position = await new Promise<GeolocationPosition>((resolve, reject) => {
        navigator.geolocation.getCurrentPosition(resolve, reject)
      })
      
      userLocation.value = {
        latitude: position.coords.latitude,
        longitude: position.coords.longitude
      }
      
      // Search for nearby providers
      await providersStore.searchProviders(userLocation.value)
    }
  } catch (err: any) {
    error.value = 'Failed to get your location. Please enable location services.'
  }
})

const handleSubmit = async () => {
  isLoading.value = true
  error.value = null
  
  try {
    await assistanceStore.createRequest({
      location: userLocation.value,
      vehicleInfo: vehicleInfo.value,
      issue: issue.value,
      notes: notes.value
    })
  } catch (err: any) {
    error.value = err.message
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="max-w-4xl mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold mb-8">Request Assistance</h1>
    
    <div v-if="error" class="bg-error-100 text-error-700 p-4 rounded-lg mb-6">
      {{ error }}
    </div>
    
    <form @submit.prevent="handleSubmit" class="space-y-6">
      <div class="bg-white rounded-lg shadow p-6">
        <h2 class="text-xl font-semibold mb-4">Vehicle Information</h2>
        
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div class="form-group">
            <label for="make" class="form-label">Make</label>
            <input
              id="make"
              v-model="vehicleInfo.make"
              type="text"
              class="input"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="model" class="form-label">Model</label>
            <input
              id="model"
              v-model="vehicleInfo.model"
              type="text"
              class="input"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="year" class="form-label">Year</label>
            <input
              id="year"
              v-model="vehicleInfo.year"
              type="text"
              class="input"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="color" class="form-label">Color</label>
            <input
              id="color"
              v-model="vehicleInfo.color"
              type="text"
              class="input"
              required
            />
          </div>
          
          <div class="form-group md:col-span-2">
            <label for="licensePlate" class="form-label">License Plate (Optional)</label>
            <input
              id="licensePlate"
              v-model="vehicleInfo.licensePlate"
              type="text"
              class="input"
            />
          </div>
        </div>
      </div>
      
      <div class="bg-white rounded-lg shadow p-6">
        <h2 class="text-xl font-semibold mb-4">Issue Details</h2>
        
        <div class="form-group">
          <label for="issue" class="form-label">What's the problem?</label>
          <textarea
            id="issue"
            v-model="issue"
            rows="3"
            class="input"
            required
          ></textarea>
        </div>
        
        <div class="form-group">
          <label for="notes" class="form-label">Additional Notes (Optional)</label>
          <textarea
            id="notes"
            v-model="notes"
            rows="2"
            class="input"
          ></textarea>
        </div>
      </div>
      
      <div class="flex justify-end space-x-4">
        <Button
          type="submit"
          variant="primary"
          size="lg"
          :loading="isLoading"
        >
          Request Help
        </Button>
      </div>
    </form>
  </div>
</template>
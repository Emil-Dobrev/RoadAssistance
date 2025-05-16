<script lang="ts">
declare global {
  interface Window {
    goToProviderProfile: (providerId: string) => void
  }
}
export {}
</script>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Loader } from '@googlemaps/js-api-loader'
import { useProvidersStore } from '../stores/providers'
import Button from '../components/ui/Button.vue'
import TestimonialsSection from "../components/home/TestimonialsSection.vue";
import {useUserLocationStore} from "../stores/userLocation.ts";
import {getCurrentLocation} from "../utils/locationUtils.ts";
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'
import {defaultAvatar} from "../utils/constants.ts";
import api from "../services/api.ts";
import {useAuthStore} from "../stores/auth.ts";

const router = useRouter()
window.goToProviderProfile = (providerId: string) => {
  router.push(`/provider/profile/${providerId}`)
}

const providersStore = useProvidersStore()
const userLocationStore = useUserLocationStore()
const authStore = useAuthStore()
const mapContainer = ref<HTMLElement | null>(null)
const map = ref<google.maps.Map | null>(null)
const markers = ref<google.maps.Marker[]>([])
const userLocation = ref<{ lat: number; lng: number } | null>(null)
const isLoading = ref(false)
const error = ref('')
const infoWindow = ref<google.maps.InfoWindow | null>(null)
const selectedDistance = ref(100) // default 100 km
const showSosModal = ref(false)
const isSendingSOS = ref(false)
const phoneNumber = ref('')


const hasLocation = computed(() => userLocation.value !== null)

const initMap = async () => {
  try {
    const loader = new Loader({
      apiKey: import.meta.env.VITE_GOOGLE_MAPS_API_KEY,
      version: 'quarterly'
    })

    await loader.load()

    if (!mapContainer.value) return

    const defaultLocation = { lat:  42.696885474356975, lng: 23.363341390400745 } // Sofia as default
    
    map.value = new google.maps.Map(mapContainer.value, {
      center: userLocation.value || defaultLocation,
      zoom: 10,
      styles: [
        {
          featureType: 'poi',
          elementType: 'labels',
          stylers: [{ visibility: 'off' }]
        }
      ]
    })

    infoWindow.value = new google.maps.InfoWindow()

    if (userLocation.value) {
      new google.maps.Marker({
        position: userLocation.value,
        map: map.value,
        icon: {
          path: google.maps.SymbolPath.CIRCLE,
          scale: 10,
          fillColor: '#0A84FF',
          fillOpacity: 1,
          strokeColor: '#ffffff',
          strokeWeight: 2
        },
        title: 'Your Location'
      })
    }
  } catch (err) {
    console.error('Error loading Google Maps:', err)
    error.value = 'Failed to load the map. Please try again later.'
  }
}

const submitSosRequest = async () => {
  if (!userLocation.value || !phoneNumber.value) return

  isSendingSOS.value = true
  try {
    // Replace with your actual API call
    await api.post('/api/v1/subscriptions/notify/sos', {
      latitude: userLocation.value.lat,
      longitude: userLocation.value.lng,
      radiusKm: selectedDistance.value,
      phoneNumber: phoneNumber.value,
      message: "Need assistance"
    })

    toast.success('SOS request sent to nearby providers!')
    showSosModal.value = false
    phoneNumber.value = ''
  } catch (error) {
    toast.error('Failed to send SOS request')
  } finally {
    isSendingSOS.value = false
  }
}

const searchNearbyProviders = async () => {
  if (!userLocation.value) return

  isLoading.value = true
  error.value = ''

  try {
    await providersStore.searchProviders({
      latitude: userLocation.value.lat,
      longitude: userLocation.value.lng,
      radiusKm: selectedDistance.value
    })

    // Clear existing markers
    markers.value.forEach(marker => marker.setMap(null))
    markers.value = []

    // Add markers for providers
    providersStore.providers.forEach(provider => {

      if (!map.value || !provider.location?.coordinates) return
      const marker = new google.maps.Marker({
        position: {
          lat: provider.location.coordinates[1] ,
          lng: provider.location.coordinates[0]
        },
        map: map.value,
        title: provider.companyName,
        icon: {
          url: 'https://maps.google.com/mapfiles/ms/icons/red-dot.png'
        }
      })

      marker.addListener('click', () => {
        if (!infoWindow.value || !map.value) return

        const content = `
<div class="p-4 w-full max-w-sm sm:max-w-md bg-white rounded-lg shadow-md">
  <div class="flex flex-col sm:flex-row items-start sm:items-center space-y-3 sm:space-y-0 sm:space-x-4">

    <!-- Avatar -->
    <img
      src="${provider.userProfilePictureUrl || defaultAvatar}"
      alt="${provider.companyName}"
      class="w-14 h-14 rounded-full object-cover border"
    />

    <!-- Content -->
    <div class="w-full">
      <div class="flex items-center justify-between">
        <h3 class="font-bold text-base sm:text-lg text-gray-800">${provider.companyName}</h3>
        ${
            provider.isNonStop
                ? `<div class="flex items-center text-green-600 text-xs sm:text-sm">
                <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                24/7
              </div>`
                : ''
        }
      </div>

      <!-- Rating with stars -->
      <div class="flex items-center text-sm text-gray-600 mt-1 mb-3">
        <div class="flex mr-2">
          ${[1, 2, 3, 4, 5]
            .map(
                (i) =>
                    `<svg class="w-4 h-4 ${
                        i <= provider.rating ? 'text-yellow-400' : 'text-gray-300'
                    }" fill="currentColor" viewBox="0 0 20 20">
                  <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.18 3.626a1 1 0 00.95.69h3.813c.969 0 1.371 1.24.588 1.81l-3.087 2.244a1 1 0 00-.364 1.118l1.18 3.626c.3.921-.755 1.688-1.54 1.118L10 13.347l-3.087 2.244c-.784.57-1.838-.197-1.539-1.118l1.18-3.626a1 1 0 00-.364-1.118L3.103 9.053c-.783-.57-.38-1.81.588-1.81h3.813a1 1 0 00.95-.69l1.18-3.626z"/>
                </svg>`
            )
            .join('')}
        </div>
        (${provider.reviewCount ?? 0} reviews)
      </div>

      <!-- View Profile Button -->
      <button
        onclick="goToProviderProfile('${provider.id}')"
        class="w-full sm:w-auto bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 text-sm"
      >
        View Profile
      </button>
    </div>
  </div>
</div>
        `

        infoWindow.value.setContent(content)
        infoWindow.value.open(map.value, marker)
      })

      markers.value.push(marker)
    })

    // Adjust map bounds to show all markers
    if (markers.value.length > 0) {
      const bounds = new google.maps.LatLngBounds()
      bounds.extend(userLocation.value)
      markers.value.forEach(marker => bounds.extend(marker.getPosition()!))
      map.value?.fitBounds(bounds)
    }
  } catch (err) {
    toast.error('Failed to find nearby service providers')
  } finally {
    isLoading.value = false
  }
}

onMounted(async () => {
  try {
    const position = await getCurrentLocation()
          .catch((error) => {
            toast.error('Please enable location in your browser settings.')
          })

      if(position) {
        userLocationStore.setLocation(
            position.coords.latitude,
            position.coords.longitude
        )

        userLocation.value = {
          lat: position.coords.latitude,
          lng: position.coords.longitude
        }
      }

    await initMap()
  } catch (err) {
    toast.error('Please enable location in your browser settings.')
    error.value = 'Please enable location services to find nearby providers'
    await initMap() // Initialize map with default location
  }
})
</script>

<template>
  <div class="min-h-screen bg-neutral-50">
    <!-- Map Section -->
    <div class="relative h-[calc(100vh-4rem)]">
      <!-- Map Container -->
      <div 
        ref="mapContainer" 
        class="w-full h-full"
      ></div>
      <!-- Overlay Controls -->
      <div class="absolute top-2 left-2 flex justify-start z-10">
        <div class="bg-white rounded-md shadow p-3 w-72">
          <h2 class="text-lg font-semibold mb-3">Find Providers</h2>
          <div class="mb-3">
            <label for="distance" class="block text-sm font-medium text-gray-700 mb-1">
              Search Radius (km)
            </label>
            <select
                id="distance"
                v-model="selectedDistance"
                class="w-full border border-gray-300 rounded-md shadow-sm text-sm"
            >
              <option :value="50">50 km</option>
              <option :value="100">100 km</option>
              <option :value="150">150 km</option>
            </select>
          </div>
          <div v-if="error" class="mb-3 p-2 bg-red-100 text-red-700 text-sm rounded">
            {{ error }}
          </div>

          <Button
              variant="primary"
              :disabled="!hasLocation || isLoading"
              :loading="isLoading"
              fullWidth
              @click="searchNearbyProviders"
          >
            Show Providers within {{ selectedDistance }}km
          </Button>
          
          <p v-if="!hasLocation" class="mt-2 text-sm text-neutral-600 text-center">
            Please enable location services to find nearby providers
          </p>
        </div>
        <div v-if="hasLocation && authStore.serviceProvider  === null" class="bg-white rounded-md shadow p-3 w-40 flex flex-col items-center justify-center">
          <h2 class="text-md font-semibold text-red-600 mb-3">SOS</h2>
          <button
              class="bg-red-600 hover:bg-red-700 text-white text-sm px-4 py-2 rounded-md w-full"
              @click="showSosModal = true"
              :disabled="!hasLocation"
          >
            🚨 Send SOS
          </button>
          <div class="mt-2 text-xs text-center text-gray-500">
            Notifies all nearby providers
          </div>
        </div>
      </div>
    </div>
    <!-- SOS Modal -->
    <Transition name="fade">
      <div
          v-if="showSosModal"
          class="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center"
      >
        <div class="bg-white rounded-xl shadow-lg w-full max-w-md p-6">
          <h3 class="text-xl font-semibold mb-2 text-red-600">🚨 SOS Alert</h3>
          <p class="text-sm text-gray-700 mb-4">
            This will notify all nearby providers within {{ selectedDistance }}km of your location. Please provide your phone number so they can reach you.
          </p>

          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">Phone Number</label>
            <input
                v-model="phoneNumber"
                type="tel"
                placeholder="e.g., +1234567890"
                class="w-full border border-gray-300 rounded-md px-3 py-2 text-sm"
            />
          </div>

          <div class="flex justify-end space-x-2">
            <button
                class="px-4 py-2 rounded-md text-sm bg-gray-200 hover:bg-gray-300"
                @click="showSosModal = false"
            >
              Cancel
            </button>
            <button
                class="px-4 py-2 rounded-md text-sm bg-red-600 text-white hover:bg-red-700"
                :disabled="!phoneNumber || isSendingSOS"
                @click="submitSosRequest"
            >
              {{ isSendingSOS ? 'Sending...' : 'Send SOS' }}
            </button>
          </div>
        </div>
      </div>
    </Transition>
<!--    testimonials section-->
    <testimonials-section/>
  </div>
</template>
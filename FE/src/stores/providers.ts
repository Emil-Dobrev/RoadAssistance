import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ServiceProvider, MapLocation } from '../types'
import { providerService } from '../services/providerService'

export const useProvidersStore = defineStore('providers', () => {
  const providers = ref<ServiceProvider[]>([])
  const selectedProvider = ref<ServiceProvider | null>(null)
  const isLoading = ref(false)
  const error = ref<string | null>(null)
  
  const nearbyProviders = computed(() => providers.value)
  
  async function searchProviders(location: MapLocation) {
    isLoading.value = true
    error.value = null

    try {
      providers.value = await providerService.searchProviders(location)

    } catch (err: any) {
      console.error('Failed to search providers:', err)
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }
  
  async function getProviderDetails(providerId: string) {
    isLoading.value = true
    error.value = null
    
    try {
      const provider = await providerService.getProviderDetails(providerId)
      selectedProvider.value = provider
      return provider
    } catch (err: any) {
      console.error('Failed to get provider details:', err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }
  
  function clearProviders() {
    providers.value = []
    selectedProvider.value = null
  }
  
  function selectProvider(providerId: string) {
    const provider = providers.value.find(p => p.id === providerId)
    if (provider) {
      selectedProvider.value = provider
    }
  }
  
  return {
    providers,
    selectedProvider,
    isLoading,
    error,
    nearbyProviders,
    searchProviders,
    getProviderDetails,
    clearProviders,
    selectProvider
  }
})
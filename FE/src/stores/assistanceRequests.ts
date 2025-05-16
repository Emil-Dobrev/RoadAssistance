import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { 
  AssistanceRequest, 
  RequestStatus,
  MapLocation
} from '../types'
import { assistanceService } from '../services/assistanceService'
import { useAuthStore } from './auth'

export const useAssistanceRequestsStore = defineStore('assistanceRequests', () => {
  const authStore = useAuthStore()
  
  const activeRequest = ref<AssistanceRequest | null>(null)
  const userRequests = ref<AssistanceRequest[]>([])
  const providerRequests = ref<AssistanceRequest[]>([])
  const isLoading = ref(false)
  const error = ref<string | null>(null)
  
  const hasActiveRequest = computed(() => !!activeRequest.value)
  
  const pendingRequests = computed(() => {
    return providerRequests.value.filter(req => 
      req.status === RequestStatus.PENDING
    )
  })
  
  const acceptedRequests = computed(() => {
    return providerRequests.value.filter(req => 
      [RequestStatus.ACCEPTED, RequestStatus.EN_ROUTE, RequestStatus.ARRIVED, RequestStatus.IN_PROGRESS].includes(req.status as RequestStatus)
    )
  })
  
  const completedRequests = computed(() => {
    return providerRequests.value.filter(req => 
      [RequestStatus.COMPLETED, RequestStatus.CANCELLED, RequestStatus.DECLINED].includes(req.status as RequestStatus)
    )
  })
  
  async function loadUserActiveRequest() {
    if (!authStore.isAuthenticated || authStore.user?.role !== 'USER') return
    
    isLoading.value = true
    error.value = null
    
    try {
      const request = await assistanceService.getUserActiveRequest()
      activeRequest.value = request
    } catch (err: any) {
      console.error('Failed to load active request:', err)
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }
  
  async function loadUserRequestHistory() {
    if (!authStore.isAuthenticated || authStore.user?.role !== 'USER') return
    
    isLoading.value = true
    error.value = null
    
    try {
      userRequests.value = await assistanceService.getUserRequestHistory()
    } catch (err: any) {
      console.error('Failed to load request history:', err)
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }
  
  async function loadProviderRequests() {
    if (!authStore.isAuthenticated || authStore.user?.role !== 'SERVICE_PROVIDER') return
    
    isLoading.value = true
    error.value = null
    
    try {
      providerRequests.value = await assistanceService.getProviderRequests()
    } catch (err: any) {
      console.error('Failed to load provider requests:', err)
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }
  
  async function createRequest(requestData: {
    location: MapLocation,
    vehicleInfo: {
      make: string,
      model: string,
      year: string,
      color: string,
      licensePlate?: string
    },
    issue: string,
    notes?: string,
    serviceId?: string,
    providerId?: string
  }) {
    isLoading.value = true
    error.value = null
    
    try {
      const newRequest = await assistanceService.createRequest(requestData)
      activeRequest.value = newRequest
      return newRequest
    } catch (err: any) {
      console.error('Failed to create request:', err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }
  
  async function updateRequestStatus(requestId: string, status: RequestStatus) {
    isLoading.value = true
    error.value = null
    
    try {
      const updatedRequest = await assistanceService.updateRequestStatus(requestId, status)
      
      if (authStore.user?.role === 'USER' && activeRequest.value?.id === requestId) {
        activeRequest.value = updatedRequest
      } else if (authStore.user?.role === 'SERVICE_PROVIDER') {
        const index = providerRequests.value.findIndex(req => req.id === requestId)
        if (index !== -1) {
          providerRequests.value[index] = updatedRequest
        }
      }
      
      return updatedRequest
    } catch (err: any) {
      console.error('Failed to update request status:', err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }
  
  async function submitReview(requestId: string, rating: number, review: string) {
    isLoading.value = true
    error.value = null
    
    try {
      const updatedRequest = await assistanceService.submitReview(requestId, rating, review)
      
      if (activeRequest.value?.id === requestId) {
        activeRequest.value = updatedRequest
      }
      
      const index = userRequests.value.findIndex(req => req.id === requestId)
      if (index !== -1) {
        userRequests.value[index] = updatedRequest
      }
      
      return updatedRequest
    } catch (err: any) {
      console.error('Failed to submit review:', err)
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }
  
  async function cancelRequest(requestId: string, reason?: string) {
    isLoading.value = true
    error.value = null
    
    try {
      await assistanceService.cancelRequest(requestId, reason)
      
      if (activeRequest.value?.id === requestId) {
        await loadUserActiveRequest() // Refresh to make sure we get latest state
      }
      
      return true
    } catch (err: any) {
      console.error('Failed to cancel request:', err)
      error.value = err.message
      return false
    } finally {
      isLoading.value = false
    }
  }
  
  return {
    activeRequest,
    userRequests,
    providerRequests,
    isLoading,
    error,
    hasActiveRequest,
    pendingRequests,
    acceptedRequests,
    completedRequests,
    loadUserActiveRequest,
    loadUserRequestHistory,
    loadProviderRequests,
    createRequest,
    updateRequestStatus,
    submitReview,
    cancelRequest
  }
})
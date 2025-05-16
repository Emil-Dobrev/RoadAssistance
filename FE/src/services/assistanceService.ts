import api from './api'
import { AssistanceRequest, RequestStatus, MapLocation } from '../types'

// Mock implementation for development without backend
let mockEnabled = true

const mockRequests: AssistanceRequest[] = [
  {
    id: '1',
    userId: '123',
    providerId: '456',
    serviceId: '789',
    status: RequestStatus.PENDING,
    location: {
      latitude: 37.7749,
      longitude: -122.4194,
      address: '123 Main St, San Francisco, CA'
    },
    vehicleInfo: {
      make: 'Toyota',
      model: 'Camry',
      year: '2019',
      color: 'Blue',
      licensePlate: 'ABC123'
    },
    issue: 'Flat tire',
    notes: 'Rear right tire is flat',
    createdAt: new Date(Date.now() - 3600000).toISOString()
  },
  {
    id: '2',
    userId: '123',
    providerId: '456',
    serviceId: '790',
    status: RequestStatus.COMPLETED,
    location: {
      latitude: 37.7749,
      longitude: -122.4194,
      address: '456 Oak St, San Francisco, CA'
    },
    vehicleInfo: {
      make: 'Honda',
      model: 'Accord',
      year: '2020',
      color: 'Red'
    },
    issue: 'Battery dead',
    createdAt: new Date(Date.now() - 86400000).toISOString(),
    acceptedAt: new Date(Date.now() - 85400000).toISOString(),
    completedAt: new Date(Date.now() - 82800000).toISOString(),
    rating: 5,
    review: 'Great service, very prompt and professional!'
  }
]

export const assistanceService = {
  async getUserActiveRequest(): Promise<AssistanceRequest | null> {
    if (mockEnabled) {
      // Simulate API call delay
      await new Promise(resolve => setTimeout(resolve, 800))
      
      // Return active request if exists
      const activeRequest = mockRequests.find(
        req => [
          RequestStatus.PENDING,
          RequestStatus.ACCEPTED,
          RequestStatus.EN_ROUTE,
          RequestStatus.ARRIVED,
          RequestStatus.IN_PROGRESS
        ].includes(req.status as RequestStatus)
      )
      
      return activeRequest || null
    }
    
    const response = await api.get<AssistanceRequest | null>('/assistance/active')
    return response.data
  },
  
  async getUserRequestHistory(): Promise<AssistanceRequest[]> {
    if (mockEnabled) {
      // Simulate API call delay
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      // Return mock request history
      return mockRequests
    }
    
    const response = await api.get<AssistanceRequest[]>('/assistance/history')
    return response.data
  },
  
  async getProviderRequests(): Promise<AssistanceRequest[]> {
    if (mockEnabled) {
      // Simulate API call delay
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      // Generate more mock data for providers
      const providerMockRequests: AssistanceRequest[] = [
        ...mockRequests,
        {
          id: '3',
          userId: '124',
          status: RequestStatus.PENDING,
          location: {
            latitude: 37.7850,
            longitude: -122.4100,
            address: '789 Pine St, San Francisco, CA'
          },
          vehicleInfo: {
            make: 'Ford',
            model: 'F-150',
            year: '2018',
            color: 'Black',
            licensePlate: 'XYZ789'
          },
          issue: 'Engine won\'t start',
          notes: 'Vehicle is in a safe location',
          createdAt: new Date(Date.now() - 1800000).toISOString()
        },
        {
          id: '4',
          userId: '125',
          providerId: '456', // Current provider
          serviceId: '791',
          status: RequestStatus.EN_ROUTE,
          location: {
            latitude: 37.7830,
            longitude: -122.4150,
            address: '321 Market St, San Francisco, CA'
          },
          vehicleInfo: {
            make: 'BMW',
            model: 'X5',
            year: '2021',
            color: 'Silver'
          },
          issue: 'Keys locked in car',
          createdAt: new Date(Date.now() - 3000000).toISOString(),
          acceptedAt: new Date(Date.now() - 2700000).toISOString()
        }
      ]
      
      return providerMockRequests
    }
    
    const response = await api.get<AssistanceRequest[]>('/assistance/provider/requests')
    return response.data
  },
  
  async createRequest(requestData: {
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
  }): Promise<AssistanceRequest> {
    if (mockEnabled) {
      // Simulate API call delay
      await new Promise(resolve => setTimeout(resolve, 1500))
      
      // Create a new mock request
      const newRequest: AssistanceRequest = {
        id: Math.random().toString(36).substring(2, 10),
        userId: '123',
        providerId: requestData.providerId,
        serviceId: requestData.serviceId,
        status: RequestStatus.PENDING,
        location: {
          latitude: requestData.location.latitude,
          longitude: requestData.location.longitude,
          address: 'Detected Address'
        },
        vehicleInfo: requestData.vehicleInfo,
        issue: requestData.issue,
        notes: requestData.notes,
        createdAt: new Date().toISOString()
      }
      
      // Add to mock requests
      mockRequests.unshift(newRequest)
      
      return newRequest
    }
    
    const response = await api.post<AssistanceRequest>('/assistance/request', requestData)
    return response.data
  },
  
  async updateRequestStatus(requestId: string, status: RequestStatus): Promise<AssistanceRequest> {
    if (mockEnabled) {
      // Simulate API call delay
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      // Find and update the request
      const mockIndex = mockRequests.findIndex(req => req.id === requestId)
      
      if (mockIndex === -1) {
        throw new Error('Request not found')
      }
      
      const updatedRequest = { ...mockRequests[mockIndex], status }
      
      // Add timestamps based on status
      if (status === RequestStatus.ACCEPTED) {
        updatedRequest.acceptedAt = new Date().toISOString()
      } else if (status === RequestStatus.COMPLETED) {
        updatedRequest.completedAt = new Date().toISOString()
      }
      
      mockRequests[mockIndex] = updatedRequest
      
      return updatedRequest
    }
    
    const response = await api.put<AssistanceRequest>(`/assistance/request/${requestId}/status`, { status })
    return response.data
  },
  
  async submitReview(requestId: string, rating: number, review: string): Promise<AssistanceRequest> {
    if (mockEnabled) {
      // Simulate API call delay
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      // Find and update the request
      const mockIndex = mockRequests.findIndex(req => req.id === requestId)
      
      if (mockIndex === -1) {
        throw new Error('Request not found')
      }
      
      const updatedRequest = { 
        ...mockRequests[mockIndex],
        rating,
        review,
        status: RequestStatus.COMPLETED
      }
      
      mockRequests[mockIndex] = updatedRequest
      
      return updatedRequest
    }
    
    const response = await api.post<AssistanceRequest>(`/assistance/request/${requestId}/review`, { rating, review })
    return response.data
  },
  
  async cancelRequest(requestId: string, reason?: string): Promise<void> {
    if (mockEnabled) {
      // Simulate API call delay
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      // Find and update the request
      const mockIndex = mockRequests.findIndex(req => req.id === requestId)
      
      if (mockIndex === -1) {
        throw new Error('Request not found')
      }
      
      mockRequests[mockIndex] = {
        ...mockRequests[mockIndex],
        status: RequestStatus.CANCELLED,
        notes: reason ? `${mockRequests[mockIndex].notes || ''}\nCancellation reason: ${reason}` : mockRequests[mockIndex].notes
      }
      
      return
    }
    
    await api.put(`/assistance/request/${requestId}/cancel`, { reason })
  }
}
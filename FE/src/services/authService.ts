import api from './api'
import {User, LoginCredentials, RegisterData, ServiceProvider} from '../types'

// Mock implementation for development without backend
let mockAuthEnabled = true

// Type for the API response
export interface AuthResponse {
  user: User | ServiceProvider
  token: string
}

export const authService = {
  async login(credentials: LoginCredentials): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('api/v1/auth/login', credentials)
    return response.data
  },
  
  async register(data: RegisterData): Promise<AuthResponse> {

    const response = await api.post<AuthResponse>('api/v1/auth/register', data)
    return response.data
  },
  
  async getCurrentUser(): Promise<User | ServiceProvider> {
    const response = await api.get<User | ServiceProvider>('api/v1/auth/me')
    return response.data
  },
  
  async forgotPassword(email: string): Promise<void> {
    if (mockAuthEnabled) {
      // Simulate API call delay
      await new Promise(resolve => setTimeout(resolve, 1000))
      return
    }
    
    await api.post('/auth/forgot-password', { email })
  }
}
import axios from 'axios'

// Set the base URL for the API
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/'

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Interceptor to add authorization token to requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// Response interceptor to handle common errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const { response } = error
    
    // Handle token expiration or invalid token
    if (response && response.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/auth/login'
    }
    
    // Create a standardized error message
    const errorMessage = 
      (response && response.data && response.data.message) || 
      'Something went wrong. Please try again later.'
    
    return Promise.reject(new Error(errorMessage))
  }
)

export default api
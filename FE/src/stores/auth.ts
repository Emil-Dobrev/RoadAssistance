import {defineStore} from 'pinia'
import {computed, ref} from 'vue'
import {useRouter} from 'vue-router'
import {LoginCredentials, RegisterData, ServiceProvider, User, UserRole} from '../types'
import { authService} from '../services/authService'

export const useAuthStore = defineStore('auth', () => {
    const router = useRouter()

    const user = ref<User | null>(null)
    const serviceProvider = ref<ServiceProvider | null>(null)
    const token = ref<string | null>(null)
    const isInitializing = ref(true)
    const error = ref<string | null>(null)
    const isLoading = ref(false)

    const isAuthenticated = computed(() => !!token.value)

    async function initializeAuth() {
        isInitializing.value = true
        try {
            const savedToken = localStorage.getItem('token')
            if (savedToken) {
                token.value = savedToken
                await fetchUserProfile()
            }
        } catch (err) {
            console.error('Failed to initialize auth:', err)
            logout()
        } finally {
            isInitializing.value = false
        }
    }

    async function login(credentials: LoginCredentials) {
        isLoading.value = true
        error.value = null

        try {
            const response = await authService.login(credentials)

            token.value = response.token
            localStorage.setItem('token', response.token)
            if (response.user.role === UserRole.USER) {
                user.value = response.user as User
            } else {
                serviceProvider.value = response.user as ServiceProvider
            }

            return true
        } catch (err: any) {
            logout(false)
            error.value = err.message || 'Failed to login'
            return false
        } finally {
            isLoading.value = false
        }
    }

    async function register(data: RegisterData) {
        isLoading.value = true
        error.value = null

        try {
            const response = await authService.register(data)
            if (response.user.role === UserRole.USER) {
                user.value = response.user as User
            } else {
                serviceProvider.value = response.user as ServiceProvider
            }
            localStorage.setItem('token', response.token)
            await login({email: data.email, password: data.password})
            return true
        } catch (err: any) {
            error.value = err.message || 'Failed to register'
            return false
        } finally {
            isLoading.value = false
        }
    }

    async function fetchUserProfile() {
        try {
            const userData = await authService.getCurrentUser()
            if (userData.role === UserRole.USER) {
                user.value = userData as User
            } else {
                serviceProvider.value = userData as ServiceProvider
            }
        } catch (err) {
            console.error('Failed to fetch user profile:', err)
            throw err
        }
    }

    function logout(shouldNavigateToHome: boolean = true) {
        user.value = null
        token.value = null
        localStorage.removeItem('token')

        // Redirect to home
        if(shouldNavigateToHome){
            router.push({name: 'Home'})
        }
    }

    return {
        user,
        serviceProvider,
        token,
        isInitializing,
        isLoading,
        error,
        isAuthenticated,
        initializeAuth,
        login,
        register,
        fetchUserProfile,
        logout
    }
})

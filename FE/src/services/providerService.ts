import api from './api'
import {
    CommentRequest,
    Coordinates,
    MapLocation,
    RatingRequest,
    ServiceProvider,
    ServiceProviderUpdateDTO
} from '../types'

export const providerService = {
    async searchProviders(location: MapLocation): Promise<ServiceProvider[]> {
        const response = await api.get<ServiceProvider[]>('/api/v1/providers/nearby', {
            params: {
                lat: location.latitude,
                lng: location.longitude,
                radiusKm: location.radiusKm
            }
        })
        return response.data
    },

    async getProviderDetails(providerId: string): Promise<ServiceProvider> {

        const response = await api.get<ServiceProvider>(`/providers/${providerId}`)
        return response.data
    },
}

export async function updateProviderLocation(providerId: string, coordinates: Coordinates) {
    try {
        await api.patch(`/api/v1/providers/location/${providerId}`, coordinates)
    }catch (error) {

    }
}

export async function getServiceProviderById(providerId: string): Promise<ServiceProvider | null> {
    try {
        const response = await api.get<ServiceProvider>(`/api/v1/providers/${providerId}`)
        return response.data
    } catch (error) {
        console.error("No such provider", error)
        return null
    }
}

export async function setProviderRating(ratingRequest: RatingRequest) {
    const response = await api.post("/api/v1/providers/rating", ratingRequest)
    return response.data
}

export async function postComment(providerId: string, comment: CommentRequest) {
    return api.post(`/api/v1/providers/comment/${providerId}`, comment)
}

export async function updateProvider(providerId: string, serviceProviderUpdateDTO: ServiceProviderUpdateDTO) {
    return await api.patch<ServiceProvider>(`/api/v1/providers/${providerId}`, serviceProviderUpdateDTO)
}

export enum UserRole {
    USER = 'USER',
SERVICE_PROVIDER = 'SERVICE_PROVIDER'
}

export type Coordinates = { latitude: number; longitude: number, coordinates?: number[] }

export type User = {
    id: string
    email: string
    firstName: string
    lastName: string
    fullName: string
    role: UserRole
    phoneNumber?: string
    avatarUrl?: string
    createdAt: string
    location?: Coordinates
    userProfilePictureUrl?: string
}

export type ServiceProvider = User & {
    companyName: string
    companyDescription?: string
    // services: Service[]
    rating: number
    reviewCount: number
    address?: string
    comments?: Comment[]
    isNonStop?: boolean
}

export type RatingRequest = {
    providerId: string,
    rating: number
}

export type Comment = {
    id: string
    author: {
        userId: string
        userFullName: string
        userProfilePictureUrl?: string
    }
    message: string
    createdAt: string
}

export type CommentRequest = {
    author: {
         userId: string,
         userFullName: string,
         userProfilePictureUrl?: string,
    }
     message: string
}

export type Service = {
    id: string
    name: string
    description: string
    price: number
    estimatedTime: string
    providerId: string
}

export enum RequestStatus {
    PENDING = 'PENDING',
    ACCEPTED = 'ACCEPTED',
    EN_ROUTE = 'EN_ROUTE',
    ARRIVED = 'ARRIVED',
    IN_PROGRESS = 'IN_PROGRESS',
    COMPLETED = 'COMPLETED',
    CANCELLED = 'CANCELLED',
    DECLINED = 'DECLINED'
}

export type AssistanceRequest = {
    id: string
    userId: string
    providerId?: string
    serviceId?: string
    status: RequestStatus
    location: {
        latitude: number
        longitude: number
        address?: string
    }
    vehicleInfo: {
        make: string
        model: string
        year: string
        color: string
        licensePlate?: string
    }
    issue: string
    notes?: string
    createdAt: string
    acceptedAt?: string
    completedAt?: string
    rating?: number
    review?: string
}

export type Notification = {
    id: string
    userId: string
    title: string
    message: string
    read: boolean
    type: 'REQUEST_UPDATE' | 'SYSTEM' | 'PAYMENT'
    relatedRequestId?: string
    createdAt: string
}

export type MapLocation = {
    latitude: number
    longitude: number
    radiusKm: number
    accuracy?: number
}

export type LoginCredentials = {
    email: string
    password: string
}

export type RegisterData = {
    email: string
    password: string
    firstName: string
    lastName: string
    phoneNumber?: string
    companyName?: string
    companyDescription?: string
    role: UserRole,
    location?: Coordinates
}


export type ServiceProviderUpdateDTO = {
    companyDescription: string,
    address: String,
    phoneNumber: String,
    isNonStop: Boolean,
}


export type Testimonial = {
    userId: string
    fullName: string
    rating: number
    quote: string
    userProfileUrl: string
}


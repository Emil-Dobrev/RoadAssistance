package emil.dobrev.roadpal.model.dto

import emil.dobrev.roadpal.model.Role
import emil.dobrev.roadpal.model.ServiceProvider
import emil.dobrev.roadpal.model.User
import org.springframework.data.mongodb.core.geo.GeoJsonPoint

data class UserResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val userName: String?,
    val role: Role,
    val fullName: String,
    @JvmField
    val location: GeoJsonPoint?,
    val companyName: String? = null,
    val phoneNumber: String? = null,
) {
    constructor(user: User) : this(
        id = user.id,
        firstName = user.firstName,
        lastName = user.lastName,
        fullName = user.fullName,
        email = user.email,
        userName = user.userName,
        role = user.roles[0],
        location = user.location,
        phoneNumber = user.phoneNumber
    )
    constructor(serviceProvider: ServiceProvider): this (
        id = serviceProvider.id,
        firstName = serviceProvider.firstName,
        lastName = serviceProvider.lastName,
        fullName = serviceProvider.fullName,
        email = serviceProvider.email,
        userName = serviceProvider.userName,
        role = serviceProvider.roles[0],
        location = serviceProvider.location,
        phoneNumber = serviceProvider.phoneNumber,
        companyName = serviceProvider.companyName
    )
}
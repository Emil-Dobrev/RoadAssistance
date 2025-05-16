package emil.dobrev.roadpal.model.dto

import emil.dobrev.roadpal.model.Role
import emil.dobrev.roadpal.model.ServiceProvider
import org.springframework.data.mongodb.core.geo.GeoJsonPoint


data class ServiceProviderDTO(
    val id: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val userName: String? = null,
    val roles: List<Role> = listOf(Role.SERVICE_PROVIDER),
    val location: GeoJsonPoint? = null,
    val companyName: String,
    val companyDescription: String? = null,
    var rating: Double? = null,
    var reviewCount: Int? = null,
    var isNonStop: Boolean,
    var address: String,
    var comments: List<ProviderCommentResponse> = emptyList()
) {


    constructor(serviceProvider: ServiceProvider, comments: List<ProviderCommentResponse> = emptyList()) : this(
        id = serviceProvider.id,
        firstName = serviceProvider.firstName,
        lastName = serviceProvider.lastName,
        phoneNumber = serviceProvider.phoneNumber ?: "",
        email = serviceProvider.email,
        userName = serviceProvider.userName,
        roles = serviceProvider.roles,
        location = serviceProvider.location,
        companyName = serviceProvider.companyName,
        companyDescription = serviceProvider.companyDescription,
        rating = serviceProvider.rating,
        reviewCount = serviceProvider.reviewCount,
        comments = comments,
        isNonStop = serviceProvider.isNonStop,
        address =  serviceProvider.address ?: ""
    )
}
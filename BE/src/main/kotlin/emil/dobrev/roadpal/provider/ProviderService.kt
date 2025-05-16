package emil.dobrev.roadpal.provider

import emil.dobrev.roadpal.auth.ServiceProviderRepository
import emil.dobrev.roadpal.exception.UserNotFoundException
import emil.dobrev.roadpal.model.dto.Coordinates
import emil.dobrev.roadpal.model.dto.ServiceProviderDTO
import emil.dobrev.roadpal.model.dto.ServiceProviderUpdateDTO
import emil.dobrev.roadpal.provider.comment.ProviderCommentService
import emil.dobrev.roadpal.utils.SecurityContextUtils
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class ProviderService(
    private val serviceProviderRepository: ServiceProviderRepository,
    private val providerCommentService: ProviderCommentService,
    private val providerRepository: ServiceProviderRepository,
) {

    fun getProviderById(providerId: String): ServiceProviderDTO {
       val provider = serviceProviderRepository.findById(providerId).orElseThrow { UserNotFoundException(providerId) }
        val comments = providerCommentService.getCommentsForProvider(providerId)
        return ServiceProviderDTO(provider, comments)
    }

    fun updateProvider(providerId: String, updateDTO: ServiceProviderUpdateDTO): ServiceProviderDTO {
        if(SecurityContextUtils.getUserIdFromSecurityContext() != providerId) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized to update this user")
        }

        val provider = providerRepository.findById(providerId)
            .orElseThrow { UserNotFoundException( providerId) }

        updateDTO.companyDescription.let { provider.companyDescription = it }
        updateDTO.address.let { provider.address = it }
        updateDTO.phoneNumber.let { provider.phoneNumber = it }
        updateDTO.isNonStop.let { provider.isNonStop = it }

        val updatedProvider = providerRepository.save(provider)
        val comments = providerCommentService.getCommentsForProvider(providerId)

        return ServiceProviderDTO(updatedProvider, comments)
    }

    fun updateProviderLocation(providerId: String, coordinates: Coordinates) {
        val provider = providerRepository.findById(providerId)
            .orElseThrow { UserNotFoundException( providerId) }
        provider.location = GeoJsonPoint(coordinates.longitude, coordinates.latitude)
        providerRepository.save(provider)
    }

    fun getNearByProviders(point: GeoJsonPoint, radiusKm: Double = 50.0): List<ServiceProviderDTO> {
        val distance = Distance(radiusKm, Metrics.KILOMETERS)
        val providers =  providerRepository.findByLocationNear(point, distance)
        return providers.map { ServiceProviderDTO(it) }
    }

}
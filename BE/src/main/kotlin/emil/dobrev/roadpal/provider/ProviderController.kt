package emil.dobrev.roadpal.provider

import emil.dobrev.roadpal.model.dto.Coordinates
import emil.dobrev.roadpal.model.dto.ServiceProviderDTO
import emil.dobrev.roadpal.model.dto.ServiceProviderUpdateDTO
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.http.HttpStatus

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/providers")
class ProviderController(
    private val providerService: ProviderService
) {
    val log: org.slf4j.Logger = LoggerFactory.getLogger(ProviderController::class.java)

    @GetMapping("/{providerId}")
    fun getProviderById(@PathVariable providerId: String): ResponseEntity<ServiceProviderDTO> {
        log.info("Request for getting provider with id: $providerId")
        return ResponseEntity.ok(providerService.getProviderById(providerId))
    }

    @PatchMapping("/location/{providerId}")
        fun updateProviderLocation(@PathVariable providerId: String, @RequestBody coordinates: Coordinates): ResponseEntity<HttpStatus> {
            providerService.updateProviderLocation(providerId, coordinates)
            return ResponseEntity.ok(HttpStatus.NO_CONTENT)
    }


    @PatchMapping("/{providerId}")
    fun updatePartialProvider(
        @PathVariable providerId: String,
        @RequestBody updateDTO: ServiceProviderUpdateDTO
    ): ResponseEntity<ServiceProviderDTO> {
        val response = providerService.updateProvider(providerId, updateDTO)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/nearby")
    fun getNearByProviders(
        @RequestParam lat: Double,
        @RequestParam lng: Double,
        @RequestParam(defaultValue = "50.0") radiusKm: Double
    ): ResponseEntity<List<ServiceProviderDTO>> {
        val point = GeoJsonPoint(lng, lat) // Point(x, y) => (longitude, latitude)
        val response = providerService.getNearByProviders(point, radiusKm)
        return ResponseEntity.ok(response)
    }

}

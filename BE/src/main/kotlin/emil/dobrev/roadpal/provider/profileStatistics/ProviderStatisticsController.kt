package emil.dobrev.roadpal.provider.profileStatistics

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/providers/stats")
class ProviderStatisticsController(
    private val providerStatisticService: ProviderStatisticService
) {


    @PostMapping()
    fun phoneNumberViewCount(@Valid @RequestBody phoneNumberRequest: PhoneNumberRequest): ResponseEntity<HttpStatus> {
        providerStatisticService.addCountToPhoneNumberView(phoneNumberRequest)
        return ResponseEntity.ok(HttpStatus.OK)
    }
}


data class PhoneNumberRequest(
    @field:NotBlank(message = "First name must not be blank")
    val providerId: String
)
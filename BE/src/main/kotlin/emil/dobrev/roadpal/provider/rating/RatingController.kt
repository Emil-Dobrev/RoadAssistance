package emil.dobrev.roadpal.provider.rating

import emil.dobrev.roadpal.utils.SecurityContextUtils
import emil.dobrev.roadpal.utils.SecurityContextUtils.Companion.isServiceProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/providers/rating")
class RatingController(
    private val ratingService: RatingService
) {


    @PostMapping
    fun rateProvider(@RequestBody rateRequest: RateRequest):ResponseEntity<HttpStatus> {
        val userId = SecurityContextUtils.getUserIdFromSecurityContext()
        // other providers can't vote
        if(isServiceProvider()) {
            throw Exception("Providers can't vote for other providers")
        }
        ratingService.rateProvider(rateRequest, userId)
        return ResponseEntity.ok(HttpStatus.OK)
    }


    data class RateRequest(
        val providerId: String,
        val rating: Double
    )
}
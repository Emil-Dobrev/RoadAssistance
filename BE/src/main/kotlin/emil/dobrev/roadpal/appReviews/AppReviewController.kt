package emil.dobrev.roadpal.appReviews

import emil.dobrev.roadpal.model.AppReview
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.constraints.*

@RestController
@RequestMapping("/api/v1/reviews")
class AppReviewController(
    private val appReviewService: AppReviewService
) {

    @PostMapping
    fun addAppReview(@RequestBody @Valid appReviewRequest: AppReviewRequest): ResponseEntity<HttpStatus> {
        appReviewService.addReview(appReviewRequest)
        return ResponseEntity.ok(HttpStatus.OK)
    }

    @GetMapping
    fun getAllReview(): ResponseEntity<List<AppReview>> {
        return ResponseEntity.ok( appReviewService.getAllReviews())
    }
}


data class AppReviewRequest(
    @field:NotBlank(message = "Quote name must not be blank")
    @field:Size(min = 1, max = 700, message = "Quote must be between 1 and 600 characters")
   val quote: String,
   @field:Min(1, message = "Rating must be at least 1")
   @field:Max(5, message = "Rating must be at most 5")
    val rating: Int,
   @field:NotBlank(message = "Full name is required")
    val fullName: String,
    val userProfileUrl: String?
)
package emil.dobrev.roadpal.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "appReviews")
data class AppReview(
    val userId: String,
    var fullName: String,
    var userProfileUrl: String? = null,
    var rating: Int,
    var quote: String
) {
    @Id
    lateinit var id: String
}
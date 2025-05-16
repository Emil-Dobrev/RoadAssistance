package emil.dobrev.roadpal.appReviews

import emil.dobrev.roadpal.model.AppReview
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AppReviewRepository: MongoRepository<AppReview, String> {

    fun findByUserId(userId: String): AppReview?
}
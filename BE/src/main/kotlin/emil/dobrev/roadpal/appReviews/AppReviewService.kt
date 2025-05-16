package emil.dobrev.roadpal.appReviews

import emil.dobrev.roadpal.model.AppReview
import emil.dobrev.roadpal.utils.SecurityContextUtils
import org.springframework.stereotype.Service

@Service
class AppReviewService(
    private val appReviewRepository: AppReviewRepository,
) {

    fun addReview(appReviewRequest: AppReviewRequest) {
        val userId = SecurityContextUtils.getUserIdFromSecurityContext()
        // check if already exist , if yes just change it
          appReviewRepository.findByUserId(userId)?.apply {
            fullName = appReviewRequest.fullName
            rating = appReviewRequest.rating
            quote = appReviewRequest.quote
            userProfileUrl = appReviewRequest.userProfileUrl
        }?.let {
            appReviewRepository.save(it)
            // leave the function if saved it
            return}

           val review  = AppReview(
                userId = userId,
                fullName = appReviewRequest.fullName,
                rating = appReviewRequest.rating,
                quote = appReviewRequest.quote,
                userProfileUrl = appReviewRequest.userProfileUrl
           )
        appReviewRepository.save(review)
    }

    fun getAllReviews(): List<AppReview> {
        return appReviewRepository.findAll()
    }
}
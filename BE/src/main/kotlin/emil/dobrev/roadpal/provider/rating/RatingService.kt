package emil.dobrev.roadpal.provider.rating

import emil.dobrev.roadpal.auth.ServiceProviderRepository
import emil.dobrev.roadpal.model.ProviderRating
import emil.dobrev.roadpal.model.ServiceProvider
import emil.dobrev.roadpal.exception.UserNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RatingService(
    private val ratingRepository: RatingRepository,
    private val providerRepository: ServiceProviderRepository
) {

    @Transactional
    fun rateProvider(rateRequest: RatingController.RateRequest, userId: String) {
        val providerId = rateRequest.providerId

        val provider = providerRepository.findById(providerId).orElseThrow { UserNotFoundException(providerId) }

        // check if user voted already for this provider
        val vote = ratingRepository.findByUserIdAndProviderId(userId, providerId)
        if(vote != null) {
            if(vote.rating == rateRequest.rating) {
                // if same vote , do nothing
                return
            }
                vote.rating = rateRequest.rating
                ratingRepository.save(vote)

        } else {
            ratingRepository.save(ProviderRating( userId = userId, providerId = providerId, rating = rateRequest.rating))
        }
        updateServiceProviderRating(provider)
    }


    private fun updateServiceProviderRating(provider: ServiceProvider) {
        val ratings = ratingRepository
            .findAllByProviderId(provider.id)
        val averageRating = ratings
            .stream()
            .mapToDouble { it.rating }
            .average()
            .orElse(0.0)

        provider.rating = averageRating
        provider.reviewCount = ratings.size

        providerRepository.save(provider)
    }
}
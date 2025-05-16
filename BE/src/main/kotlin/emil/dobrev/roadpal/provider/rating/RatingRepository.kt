package emil.dobrev.roadpal.provider.rating

import emil.dobrev.roadpal.model.ProviderRating
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RatingRepository: MongoRepository<ProviderRating, String> {

    fun  findByUserIdAndProviderId(userId: String, providerId: String): ProviderRating?

    fun findAllByProviderId(providerId: String): List<ProviderRating>

}
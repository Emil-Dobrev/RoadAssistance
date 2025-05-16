package emil.dobrev.roadpal.provider.profileStatistics

import emil.dobrev.roadpal.model.ProviderStatistics
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProviderStatisticsRepository: MongoRepository<ProviderStatistics, String> {

    fun findByProviderId(providerId: String): ProviderStatistics?
}
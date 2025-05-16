package emil.dobrev.roadpal.notification

import emil.dobrev.roadpal.model.PushSubscription
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository


@Repository
interface PushSubscriptionRepository : MongoRepository<PushSubscription, String> {
    fun findByProviderId(providerId: String): List<PushSubscription>
    fun deleteAllByProviderId(providerId: String)
    fun findByProviderIdIn(providersId: List<String>): List<PushSubscription>
}
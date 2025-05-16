package emil.dobrev.roadpal.provider.comment

import emil.dobrev.roadpal.model.ProviderComment
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProviderCommentRepository : MongoRepository<ProviderComment, String> {

    fun countByProviderIdAndAuthor_UserId(providerId: String, userId: String): Long

    fun findAllByProviderId(providerId: String): List<ProviderComment>?
}
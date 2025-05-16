package emil.dobrev.roadpal.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "providersRating")
@CompoundIndex(def = "{'userId' : 1, 'providerId': 1}")
data class ProviderRating(
    val userId: String,
    val providerId: String,
    var rating: Double
) {
    @Id
    lateinit var id: String
}
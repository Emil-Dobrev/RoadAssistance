package emil.dobrev.roadpal.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "providerStatistics")
data class ProviderStatistics(
    var phoneNumberViewedCount: Int,
    val providerId: String
) {
    @Id
    lateinit var id: String
}
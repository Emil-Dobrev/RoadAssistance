package emil.dobrev.roadpal.model

import emil.dobrev.roadpal.model.dto.ProviderCommentRequest

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "providerComments")
data class ProviderComment(
    val providerId: String,
    val author: Author,
    val message: String

) {
    @Id
    lateinit var id: String
    val createdAt: Instant = Instant.now()

    constructor(providerComment: ProviderCommentRequest, providerId: String) : this(
        providerId = providerId,
        author = providerComment.author,
        message = providerComment.message
    )
}
data class Author(
    val userId: String,
    val userFullName: String,
    val userProfilePictureUrl: String? = null,
)
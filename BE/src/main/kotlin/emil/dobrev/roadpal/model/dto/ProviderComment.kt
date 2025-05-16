package emil.dobrev.roadpal.model.dto

import emil.dobrev.roadpal.model.Author
import emil.dobrev.roadpal.model.ProviderComment
import java.time.Instant

data class ProviderCommentRequest(
    val author: Author,
    val message: String
)

data class ProviderCommentResponse(
    val id: String,
    val author: Author,
    val message: String,
    val createdAt: Instant
) {
    constructor(providerComment: ProviderComment): this(
        id = providerComment.id,
        author = providerComment.author,
        message = providerComment.message,
        createdAt = providerComment.createdAt
    )
}
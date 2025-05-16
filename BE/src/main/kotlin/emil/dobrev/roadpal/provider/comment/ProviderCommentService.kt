package emil.dobrev.roadpal.provider.comment

import emil.dobrev.roadpal.auth.ServiceProviderRepository
import emil.dobrev.roadpal.model.dto.ProviderCommentRequest
import emil.dobrev.roadpal.model.dto.ProviderCommentResponse
import emil.dobrev.roadpal.model.ProviderComment
import emil.dobrev.roadpal.exception.CommentException
import emil.dobrev.roadpal.exception.UserNotFoundException

import org.springframework.stereotype.Service

@Service
class ProviderCommentService(
    private val providerCommentRepository: ProviderCommentRepository,
    private val providerRepository: ServiceProviderRepository
) {

    fun postComment(providerId: String, comment: ProviderCommentRequest) {
        // if provider dont exist throw exception
        providerRepository.findById(providerId).orElseThrow { UserNotFoundException(providerId) }
        val writtenCommentsForThisProvider =
            providerCommentRepository.countByProviderIdAndAuthor_UserId(providerId, comment.author.userId)
        // cant write more than 10 comments for one supplier
        if(writtenCommentsForThisProvider > 10) {
            throw CommentException()
        }
        providerCommentRepository.save( ProviderComment(comment, providerId))
    }

    fun getCommentsForProvider(providerId: String): List<ProviderCommentResponse> {
        val comments = providerCommentRepository.findAllByProviderId(providerId) ?: emptyList()
        return comments
            .map { ProviderCommentResponse(it) }
            .toList()
    }
}
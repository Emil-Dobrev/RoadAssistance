package emil.dobrev.roadpal.provider.comment

import emil.dobrev.roadpal.model.dto.ProviderCommentRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/providers/comment")
class CommentController(
    private val providerCommentService: ProviderCommentService
) {


    @PostMapping("/{providerId}")
    fun postComment(@PathVariable providerId: String,
                    @RequestBody comment: ProviderCommentRequest): ResponseEntity<HttpStatus> {
        providerCommentService.postComment(providerId, comment)
        return ResponseEntity.ok(HttpStatus.OK)
    }


}



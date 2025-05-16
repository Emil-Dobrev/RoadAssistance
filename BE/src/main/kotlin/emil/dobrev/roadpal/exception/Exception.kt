package emil.dobrev.roadpal.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class UserNotFoundException(searchValue: String): ResponseStatusException( HttpStatus.NOT_FOUND, "User not found: $searchValue")
class CommentException(): Exception("User can't write more than 10 comments for one provider")
class RequestNotPermitted(): ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests - please try again later.")
package emil.dobrev.roadpal.auth

import com.fasterxml.jackson.annotation.JsonProperty
import emil.dobrev.roadpal.model.dto.Coordinates
import emil.dobrev.roadpal.model.dto.UserResponse
import emil.dobrev.roadpal.model.Role
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {
    val log: Logger = LoggerFactory.getLogger(AuthenticationController::class.java)

    @PostMapping("/register")
    fun userRegister(@RequestBody @Valid userRegistrationRequest: UserRegistrationRequest): ResponseEntity<AuthenticationResponse> {
        log.info("Register request")
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(authenticationService.register(userRegistrationRequest))
    }

    @PostMapping("/login")
    fun userLogin(@RequestBody @Valid authenticationRequest: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        log.info("Login request")
        return ResponseEntity.ok().body(authenticationService.authenticate(authenticationRequest))
    }


    @GetMapping("/me")
    fun getUser(): ResponseEntity<Any> {
        return ResponseEntity.ok(authenticationService.getUser())
    }
}


data class AuthenticationResponse(
    val user: UserResponse,
    @JsonProperty("token")
    val token: String
)

data class UserRegistrationRequest(
    @field:NotBlank(message = "First name must not be blank")
    @field:Size(min = 1, max = 20, message = "First name must be between 1 and 20 characters")
    val firstName: String,
    @field:NotBlank(message = "Last name must not be blank")
    @field:Size(min = 1, max = 20, message = "Last name must be between 1 and 20 characters")
    val lastName: String,
    @field:Email
    val email: String,
    val userName: String?,
    @field:NotBlank(message = "Password must not be blank")
    @field:Size(min = 1, max = 40, message = "Password must be between 1 and 40 characters")
    val password: String,
    val role: Role,
    @JvmField
    val location: Coordinates?,
    val companyName: String? = null,
    val phoneNumber: String? = null,
//     val servicesOffered: List<String>? = null,
//     val address: String? = null,
//     val certification: List<String>? = null
)


data class AuthenticationRequest(
    @field:NotBlank
    @field:Email
    val email: String,
    @field:NotBlank
    val password: String
)

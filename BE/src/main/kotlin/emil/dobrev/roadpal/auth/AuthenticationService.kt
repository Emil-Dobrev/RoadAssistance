package emil.dobrev.roadpal.auth

import emil.dobrev.roadpal.model.dto.UserResponse
import emil.dobrev.roadpal.model.Role
import emil.dobrev.roadpal.model.ServiceProvider
import emil.dobrev.roadpal.model.User
import emil.dobrev.roadpal.utils.SecurityContextUtils.Companion.getUserIdFromSecurityContext
import emil.dobrev.roadpal.utils.SecurityContextUtils.Companion.isServiceProvider
import emil.dobrev.roadpal.exception.UserNotFoundException
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    val userRepository: UserRepository,
    val serviceProviderRepository: ServiceProviderRepository,
    val passwordEncoder: PasswordEncoder,
    val jwtService: JwtService,
    val authenticationManager: AuthenticationManager
) {

    fun register(userRegistrationRequest: UserRegistrationRequest): AuthenticationResponse {
        // Check the role and create either User or ServiceProvider
        when (userRegistrationRequest.role) {
            Role.USER -> {
                // Create a User instance and save it
                val user = User(
                    firstName = userRegistrationRequest.firstName,
                    lastName = userRegistrationRequest.lastName,
                    phoneNumber = userRegistrationRequest.phoneNumber,
                    email = userRegistrationRequest.email,
                    userName = userRegistrationRequest.userName,
                    password = passwordEncoder.encode(userRegistrationRequest.password),
                    location = userRegistrationRequest.location?.let {
                        GeoJsonPoint(
                            it.longitude,
                            userRegistrationRequest.location.latitude)
                    }
                ).also {
                    checkForExistingUserWithThisEmail(userRegistrationRequest.email, false)
                    userRepository.save(it)
                }

                val jwtToken: String = jwtService.generateToken(user)
                return AuthenticationResponse(user = UserResponse(user), jwtToken)
            }

            Role.SERVICE_PROVIDER -> {
                // Create a ServiceProvider instance and save it
                val serviceProvider = ServiceProvider(
                    firstName = userRegistrationRequest.firstName,
                    lastName = userRegistrationRequest.lastName,
                    email = userRegistrationRequest.email,
                    userName = userRegistrationRequest.userName,
                    password = passwordEncoder.encode(userRegistrationRequest.password),
                    location = userRegistrationRequest.location?.let {
                        GeoJsonPoint(
                            it.longitude,
                            userRegistrationRequest.location.latitude)
                    },
                    companyName = userRegistrationRequest.companyName ?: throw Exception("mandatory field missing"),
                    phoneNumber = userRegistrationRequest.phoneNumber ?: throw Exception("mandatory field missing"),
                ).also {
                    checkForExistingUserWithThisEmail(userRegistrationRequest.email)
                    serviceProviderRepository.save(it)
                }

                val jwtToken: String = jwtService.generateToken(serviceProvider)
                return AuthenticationResponse(user = UserResponse(serviceProvider), jwtToken)
            }

            else -> throw IllegalArgumentException("Invalid role provided")
        }
    }

    fun getUser(): Any {
        val userId = getUserIdFromSecurityContext()
        if(isServiceProvider()) {
            val serviceProvider = serviceProviderRepository.findById(userId).orElseThrow{ UserNotFoundException(userId) }
            return  UserResponse(serviceProvider)
        } else {
            val user = userRepository.findById(userId).orElseThrow{ UserNotFoundException(userId) }
            return UserResponse(user)
        }
    }

    fun authenticate(authenticationRequest: AuthenticationRequest): AuthenticationResponse {
        val authenticationToken = UsernamePasswordAuthenticationToken(
            authenticationRequest.email,
            authenticationRequest.password
        )

        authenticationManager.authenticate(authenticationToken)

        val user: User? = userRepository.findByEmail(authenticationRequest.email)
        val response: UserResponse
        val jwtToken: String

        if (user != null) {
            response = UserResponse(user)
            jwtToken = getToken(user)
        } else {
            val serviceProvider = serviceProviderRepository.findByEmail(authenticationRequest.email)
                ?: throw UsernameNotFoundException("User not found")

            response = UserResponse(serviceProvider)
            jwtToken = getToken(serviceProvider)
        }
        return AuthenticationResponse(user = response, token = jwtToken)
    }

    private fun getToken(user: User): String {
        return jwtService.generateToken(user)
    }

    private fun checkForExistingUserWithThisEmail( email: String, userRepo: Boolean = true) {
      val user =  if(userRepo) {
            userRepository.findByEmail(email)
        } else {
            serviceProviderRepository.findByEmail(email)
      }
        if(user != null) {
            throw Exception("User already exist")
        }
    }
}


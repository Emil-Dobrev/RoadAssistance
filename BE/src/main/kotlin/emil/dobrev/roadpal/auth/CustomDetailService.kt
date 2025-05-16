package emil.dobrev.roadpal.auth

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
    private val serviceProviderRepository: ServiceProviderRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        // Check for User first
        val user = userRepository.findByEmail(username)
        if (user != null) {
            return user
        }

        // If not found in User, check for ServiceProvider
        val serviceProvider = serviceProviderRepository.findByEmail(username)
        if (serviceProvider != null) {
            return serviceProvider
        }

        // If neither is found, throw exception
        throw UsernameNotFoundException("User not found with email: $username")
    }
}
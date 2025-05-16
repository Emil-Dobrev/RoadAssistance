package emil.dobrev.roadpal.users

import emil.dobrev.roadpal.auth.ServiceProviderRepository
import emil.dobrev.roadpal.auth.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val serviceProviderRepository: ServiceProviderRepository
) {



}
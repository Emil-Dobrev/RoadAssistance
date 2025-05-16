package emil.dobrev.roadpal.utils

import emil.dobrev.roadpal.model.Role
import emil.dobrev.roadpal.model.User
import org.springframework.security.core.context.SecurityContextHolder


class SecurityContextUtils {
    companion object {

        fun isServiceProvider(): Boolean {
            val authentication = SecurityContextHolder.getContext().authentication
            return authentication.authorities.any { it.authority == Role.SERVICE_PROVIDER.name}
        }


        fun getUserIdFromSecurityContext(): String {
            val authentication = SecurityContextHolder.getContext().authentication
            if (authentication == null || !authentication.isAuthenticated) {
                throw IllegalStateException("User not authenticated in the security context")
            }
            val user = authentication.principal as? User
                ?: throw IllegalStateException("User not found in security context")

            return user.id
        }
    }
}
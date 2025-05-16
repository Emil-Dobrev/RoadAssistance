package emil.dobrev.roadpal.config

import emil.dobrev.roadpal.auth.CustomUserDetailsService
import emil.dobrev.roadpal.auth.ServiceProviderRepository
import emil.dobrev.roadpal.auth.UserRepository
import jakarta.annotation.PostConstruct
import nl.martijndwars.webpush.PushService
import nl.martijndwars.webpush.Utils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

@Configuration
class AppConfig(
    private val userRepository: UserRepository,
    private val serviceProviderRepository: ServiceProviderRepository,
    private val customUserDetailsService: CustomUserDetailsService
) {
    @PostConstruct
    fun registerBouncyCastle() {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(BouncyCastleProvider())
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { email: String ->

            userRepository.findByEmail(email) ?:
                serviceProviderRepository.findByEmail(email) ?:
                throw UsernameNotFoundException("User not found with email: $email")
        }
    }

    @Bean
    fun userAuthenticationProvider(): AuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(customUserDetailsService)
        authenticationProvider.setPasswordEncoder(passwordEncoder())
        return authenticationProvider
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        val providers = listOf(
            userAuthenticationProvider()
        )
        return ProviderManager(providers)
    }

    @Bean
    fun asyncTaskExecutor(): TaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 5 // Set the number of threads
        executor.maxPoolSize = 10
        executor.queueCapacity = 25
        executor.initialize()
        return executor
    }

    @Bean
    fun createPushService(): PushService {
         val publicKeyVapid: String = "BPNBYPHNlkndzue9SQ4MfD6vv4BG7Ddnf5Z2_l2Ovk0pH4ZGRhG6-7tqcaJ40VDhd20xgGu5hx4REyM_IWASapU"
         val privateKeyVapid: String = "oezOy1JNyQfCMz0dfBTb841-MR0oTKB6M2oxct50UuI"

        return  PushService().apply {
            publicKey = Utils.loadPublicKey(publicKeyVapid)
            privateKey = Utils.loadPrivateKey(privateKeyVapid)
            setSubject("mailto:dobrev93sl@gmail.com")
        }
    }
}
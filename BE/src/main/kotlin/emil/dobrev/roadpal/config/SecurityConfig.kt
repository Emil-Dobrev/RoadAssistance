package emil.dobrev.roadpal.config


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthenticationFilter,
    private val authenticationProvider: AuthenticationProvider
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors()
            .and()
            .csrf{it.disable()}
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.POST, "/api/v1/auth/*").permitAll()

                it.requestMatchers(HttpMethod.POST, "/api/v1/providers/rating").authenticated()
                it.requestMatchers(HttpMethod.POST, "/api/v1/providers/comment/**").authenticated()
                it.requestMatchers(HttpMethod.PATCH, "/api/v1/providers/**").authenticated()
                it.requestMatchers(HttpMethod.PATCH, "/api/v1/providers/location/**").authenticated()
                it.requestMatchers(HttpMethod.GET, "/api/v1/providers/**").permitAll()
                it.requestMatchers(HttpMethod.POST, "/api/v1/providers/stats").permitAll()

                it.requestMatchers(HttpMethod.GET, "/api/v1/reviews").permitAll()
                it.requestMatchers(HttpMethod.POST, "/api/v1/reviews").authenticated()

                it.requestMatchers(HttpMethod.POST, "/api/v1/subscriptions").permitAll()
                it.requestMatchers(HttpMethod.POST, "/api/v1/subscriptions/notify/sos").permitAll()
                it.anyRequest().authenticated()
            }
            .authenticationProvider(authenticationProvider)
            .sessionManagement{ it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            // Allow the Vue frontend origin (use your actual URL in prod)
            allowedOrigins = listOf("http://localhost:5173")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
            allowedHeaders = listOf("*")
            allowCredentials = true
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }
    }
}
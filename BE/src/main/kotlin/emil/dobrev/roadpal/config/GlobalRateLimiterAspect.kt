package emil.dobrev.roadpal.config

import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Aspect
@Component
class GlobalRateLimiterAspect(private val rateLimiterRegistry: RateLimiterRegistry) {

    private val rateLimiter: RateLimiter = rateLimiterRegistry.rateLimiter("globalRateLimiter")

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    fun controllerMethods() {
    }

    @Around("controllerMethods()")
    fun rateLimit(joinPoint: org.aspectj.lang.ProceedingJoinPoint): Any? {
        if (rateLimiter.acquirePermission()) {
            return joinPoint.proceed()
        } else {
            throw emil.dobrev.roadpal.exception.RequestNotPermitted()
        }
    }
}
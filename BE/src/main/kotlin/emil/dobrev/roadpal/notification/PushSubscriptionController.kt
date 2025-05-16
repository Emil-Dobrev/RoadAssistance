package emil.dobrev.roadpal.notification

import emil.dobrev.roadpal.model.PushSubscription
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/subscriptions")
class PushSubscriptionController(private val pushSubscriptionService: PushSubscriptionService) {

    @PostMapping("/{providerId}")
    fun subscribe(
        @PathVariable providerId: String,
        @RequestBody subscription: PushSubscriptionDTO
    ): ResponseEntity<HttpStatus> {
        // Assuming userId is part of the subscription object
        pushSubscriptionService.saveSubscription(providerId, subscription)
        return ResponseEntity.ok(HttpStatus.CREATED)
    }

    @GetMapping("/{providerId}")
    fun getUserSubscriptions(@PathVariable providerId: String): List<PushSubscription> {
        return pushSubscriptionService.getSubscriptionsByUserId(providerId)
    }


    @DeleteMapping("/{providerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unsubscribe(@PathVariable providerId: String) {
        pushSubscriptionService.deleteSubscriptionByUserId(providerId)
    }

    @PostMapping("/notify/sos")
    fun sendNotification(@RequestBody sos: SOS) {
        pushSubscriptionService.sendSosNotifications(sos)
    }
    }
    data class PushSubscriptionDTO(
        val endpoint: String,
        val keys: Keys,
        val expirationTime: Long? = null
    )

    data class Keys(
        val p256dh: String,
        val auth: String
    )

    data class SOS(
         val lat: Double,
         val lng: Double,
         val radiusKm: Double,
         val message: String
    )

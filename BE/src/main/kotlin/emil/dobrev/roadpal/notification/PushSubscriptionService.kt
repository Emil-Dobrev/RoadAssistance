package emil.dobrev.roadpal.notification

import nl.martijndwars.webpush.Notification

import org.jose4j.lang.JoseException
import org.springframework.stereotype.Service
import java.security.GeneralSecurityException

import emil.dobrev.roadpal.model.PushSubscription
import emil.dobrev.roadpal.provider.ProviderService
import nl.martijndwars.webpush.PushService
import org.springframework.data.mongodb.core.geo.GeoJsonPoint


@Service
class PushSubscriptionService(
    private val pushSubscriptionRepository: PushSubscriptionRepository,
    private val pushService: PushService,
    private val providerService: ProviderService
) {


    fun sendPushNotification(subscription: PushSubscription, message: String) {
        try {
            val payload = message.toByteArray()
            val notification = Notification(
                subscription.endpoint,
                subscription.p256dh,
                subscription.auth,
                payload
            )

            pushService.send(notification)
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        } catch (e: JoseException) {
            e.printStackTrace()
        }
    }

    fun saveSubscription(providerId: String, subscription: PushSubscriptionDTO): PushSubscription {
        val existingSubscription = pushSubscriptionRepository.findByProviderId(providerId).firstOrNull()
        return if (existingSubscription != null) {
            pushSubscriptionRepository.save(existingSubscription.copy(
                endpoint = subscription.endpoint,
                p256dh = subscription.keys.p256dh,
                auth = subscription.keys.auth,
                expirationTime = subscription.expirationTime
            ))
        } else {
            pushSubscriptionRepository.save(PushSubscription(subscription, providerId))
        }
    }

    fun getSubscriptionsByUserId(userId: String): List<PushSubscription> =
        pushSubscriptionRepository.findByProviderId(userId)

    fun deleteSubscriptionByUserId(userId: String) {
        pushSubscriptionRepository.deleteAllByProviderId(userId)
    }

    fun sendSosNotifications(sos: SOS) {
        val point = GeoJsonPoint(sos.lng, sos.lat)
        // find near providers
       val nearProviders =  providerService.getNearByProviders(point, sos.radiusKm).map { it.id }
        // get subscriptions from suppliers
        val subscription = pushSubscriptionRepository.findByProviderIdIn(nearProviders)
        // send notification
        subscription.forEach{ sendPushNotification(it, sos.message)}
    }
}

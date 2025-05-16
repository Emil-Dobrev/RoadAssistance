package emil.dobrev.roadpal.model

import emil.dobrev.roadpal.notification.PushSubscriptionDTO
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "subscriptions")
data class PushSubscription(
    val providerId: String,
    val endpoint: String,
    val p256dh: String,
    val auth: String,
    val expirationTime: Long? = null
) {
    @Id lateinit var  id: String

    constructor(pushSubscriptionDTO: PushSubscriptionDTO, userId: String): this(
        providerId = userId,
        endpoint = pushSubscriptionDTO.endpoint,
        p256dh = pushSubscriptionDTO.keys.p256dh,
        auth = pushSubscriptionDTO.keys.auth,
        expirationTime = pushSubscriptionDTO.expirationTime
    )
}
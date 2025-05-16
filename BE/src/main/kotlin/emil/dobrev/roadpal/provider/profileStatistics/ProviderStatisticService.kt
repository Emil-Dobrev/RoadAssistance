package emil.dobrev.roadpal.provider.profileStatistics

import emil.dobrev.roadpal.model.ProviderStatistics
import org.springframework.stereotype.Service

@Service
class ProviderStatisticService(
    private val providerStatisticsRepository: ProviderStatisticsRepository
) {
    fun addCountToPhoneNumberView(phoneNumberRequest: PhoneNumberRequest) {
       val stats =  providerStatisticsRepository.findByProviderId(phoneNumberRequest.providerId)

        // if no stats, create new , otherwise just add one to the count
        if(stats == null) {
           val providerStats =  ProviderStatistics(phoneNumberViewedCount =  1 , providerId = phoneNumberRequest.providerId)
            providerStatisticsRepository.save(providerStats)
        } else {
            stats.phoneNumberViewedCount += 1
            providerStatisticsRepository.save(stats)
        }
    }
}
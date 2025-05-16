package emil.dobrev.roadpal.serviceTest

import emil.dobrev.roadpal.auth.ServiceProviderRepository
import emil.dobrev.roadpal.model.ProviderRating
import emil.dobrev.roadpal.model.ServiceProvider
import emil.dobrev.roadpal.provider.rating.RatingController
import emil.dobrev.roadpal.provider.rating.RatingRepository
import emil.dobrev.roadpal.provider.rating.RatingService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*


@ExtendWith(MockitoExtension::class)
class RatingServiceTest {

    @Mock
    lateinit var ratingRepository: RatingRepository

    @Mock
    lateinit var providerRepository: ServiceProviderRepository

    @InjectMocks
    lateinit var ratingService: RatingService



    @Test
    fun `should update rating when user provides a new rating`() {
        // Given
        val rateRequest = RatingController.RateRequest(
            providerId = "1", // provider ID
            rating = 5.0
        )
        val userId = "1" // user ID



        val existingProvider = ServiceProvider(
            companyName = "Some Company",
            email = "example@email.com",
            firstName = "John",
            lastName = "Doe",
            password = "somepassword123",
            phoneNumber = "123456789",
            rating = 0.0,
            reviewCount = 0
        ).apply { id=  "1" }

        val existingRating = ProviderRating(userId = userId, providerId = "1", rating = 3.0)

        // Mock repository responses
        Mockito.`when`(providerRepository.findById("1")).thenReturn(Optional.of(existingProvider))
        Mockito.`when`(ratingRepository.findByUserIdAndProviderId(userId, "1")).thenReturn(existingRating)
        Mockito.`when`(ratingRepository.findAllByProviderId("1")).thenReturn(listOf(existingRating))

        // When
        ratingService.rateProvider(rateRequest, "1")

        // Then
        Mockito.verify(ratingRepository).save(existingRating)
        Mockito.verify(providerRepository).save(existingProvider)

        assert(existingRating.rating == 5.0)
        assert(existingProvider.rating == 5.0)
    }
}

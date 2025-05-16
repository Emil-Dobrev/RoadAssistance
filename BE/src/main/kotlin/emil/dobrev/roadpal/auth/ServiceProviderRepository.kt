package emil.dobrev.roadpal.auth

import org.springframework.data.geo.Point
import emil.dobrev.roadpal.model.ServiceProvider
import org.springframework.data.domain.Pageable
import org.springframework.data.geo.Distance
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface ServiceProviderRepository: MongoRepository<ServiceProvider, String> {
    @Query("{'roles':  'USER'}")
    fun findAllByCustomQuery(pageable: Pageable): MutableList<ServiceProvider>

    fun findByEmail(email: String): ServiceProvider?

    fun findByUserName(username: String): ServiceProvider?

    fun findByLocationNear(
        point: Point,
        distance: Distance
    ): List<ServiceProvider>
}
package emil.dobrev.roadpal.auth

import emil.dobrev.roadpal.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, String> {
    @Query("{'roles':  'USER'}")
    fun findAllByCustomQuery(pageable: Pageable): MutableList<User>

    fun findByEmail(email: String): User?

    fun findByUserName(username: String): User?
}
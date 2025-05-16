package emil.dobrev.roadpal.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.Instant


@Document("users")
 open class User(
    val firstName: String,
    val lastName: String,
    var phoneNumber: String?,
    @Indexed(unique = true)
    val email: String,
    @JvmField
    var userName: String? = null,
    @JvmField
    var password: String,
    var roles: List<Role> = mutableListOf(Role.USER),
    @JvmField
    @GeoSpatialIndexed(name = "location_index", type = GeoSpatialIndexType.GEO_2DSPHERE)
    var location: GeoJsonPoint? = null,
    val avatarUrl: String? = null,
) : UserDetails {

    @Id
    lateinit var id: String
    val createdAt: Instant = Instant.now()
     var fullName: String = "$firstName $lastName"

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.stream().map { SimpleGrantedAuthority(it.name) }.toList()
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
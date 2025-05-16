package emil.dobrev.roadpal.model

import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.mapping.Document



@Document("serviceProviders")
@CompoundIndexes(
    CompoundIndex(name = "location_index", def = "{'location': '2dsphere'}")
)
class ServiceProvider(
    firstName: String,
    lastName: String,
    phoneNumber: String,
    email: String,
    userName: String? = null,
    password: String,
    roles: List<Role> = listOf(Role.SERVICE_PROVIDER),
    location: GeoJsonPoint? = null,
    var isNonStop: Boolean = false,
    val companyName: String,
    var companyDescription: String? = null,
    var rating: Double? = null,
    var reviewCount: Int? = null,
    var address: String? = null,
//    val servicesOffered: List<String>,
//    val certification: List<String>? = null
) : User(firstName, lastName,phoneNumber, email, userName, password, roles, location)
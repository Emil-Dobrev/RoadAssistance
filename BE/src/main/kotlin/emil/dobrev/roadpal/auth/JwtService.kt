package emil.dobrev.roadpal.auth

import emil.dobrev.roadpal.model.User
import emil.dobrev.user_registry.constant.Constants
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import java.util.function.Function
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys

@Service
class JwtService( @Value("\${spring.secret}") val jwtSecret: String) {
    private val  EXPIRATION_TIME_JWT = 10000 * 60 * 4

    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    fun generateToken(claims: Map<String, Any>, userDetails: UserDetails): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME_JWT))
            .signWith(getSigningKey())
            .compact()
    }

    fun generateToken(userDetails: User): String {
        val claimsMap: MutableMap<String, Any> = HashMap()
        val roles = userDetails.authorities.map(GrantedAuthority::getAuthority)
        claimsMap[Constants.ROLES] = roles
        claimsMap["userId"] = userDetails.id
        return generateToken(claimsMap, userDetails)
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    private fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getSigningKey(): Key {
        val keyBytes: ByteArray = Decoders.BASE64.decode(jwtSecret)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}
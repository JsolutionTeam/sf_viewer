package kr.co.jsol.common.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import kr.co.jsol.common.exception.UnauthorizedException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider(private val userDetailsService: UserDetailsService) {
    val log: Logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)

//    private var secretKey: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private var secretKey: Key = Keys.hmacShaKeyFor("abcdefghijklmnopqrstuvwxyz1234567890".toByteArray())

    private val accessTokenValidTime = 30 * 60 * 1000L

    private val refreshTokenValidTime = 30 * 24 * 60 * 60 * 1000L

    private fun createToken(userPk: String, validTime: Long): String {
        val claims: Claims = Jwts.claims().setSubject(userPk)
        claims["userPk"] = userPk
        val now = Date()
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + validTime))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun createAccessToken(userPk: String): String {
        return createToken(userPk, accessTokenValidTime)
    }

    fun createRefreshToken(userPk: String): String {
        return createToken(userPk, refreshTokenValidTime)
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails = userDetailsService.loadUserByUsername(getUserPk(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getUserPk(token: String): String {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).body.subject
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val token = request.getHeader("Authorization")
        return if (token != null && token.indexOf("Bearer ") > -1) token.replace("Bearer ", "") else ""
    }

//    fun validateToken(token: String): Boolean {
//        return try {
//            val claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
//            !claims.body.expiration.before(Date())
//        } catch (e: JwtException) {
//            false
//        }
//    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
            true
        } catch (e: SecurityException) {
            log.error("Invalid JWT security - message : ${e.message}")
            throw UnauthorizedException("Invalid JWT security, please update token")
        } catch (e: MalformedJwtException) {
            log.error("Invalid JWT token - message : ${e.message}")
            throw UnauthorizedException("Invalid JWT token, please update token")
        } catch (e: ExpiredJwtException) {
            log.error("Expired JWT token - message : ${e.message}")
            throw UnauthorizedException("Expired JWT token, please update token")
        } catch (e: UnsupportedJwtException) {
            log.error("Unsupported JWT token - message : ${e.message}")
            throw UnauthorizedException("Unsupported JWT token, please update token")
        } catch (e: IllegalArgumentException) {
            log.error("JWT claims string is empty - message : ${e.message}")
            throw UnauthorizedException("JWT claims string is empty, please update token")
        }
    }
}

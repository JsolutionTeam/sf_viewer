package kr.co.jsol.api.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import kr.co.jsol.api.entity.user.User
import kr.co.jsol.api.entity.user.UserDetailServiceImpl
import kr.co.jsol.api.entity.user.UserRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.stream.Collectors
import javax.security.auth.login.AccountNotFoundException
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider(private val userDetailsService: UserDetailServiceImpl) {
    private var secretKey: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

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

    fun validateToken(token: String): Boolean {
        return try {
            val claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
            !claims.body.expiration.before(Date())
        } catch (e: JwtException) {
            false
        }
    }
}

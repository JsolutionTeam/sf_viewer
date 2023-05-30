package kr.co.jsol.common.jwt.filter

import kr.co.jsol.common.jwt.JwtTokenProvider
import org.apache.commons.lang3.mutable.Mutable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
) : OncePerRequestFilter() {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    private val TOKEN_PASSER: List<String> = listOf(
        "/swagger",
        "login",
    )

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val res: HttpServletResponse = response
        res.setHeader("Access-Control-Allow-Origin", "*")
        res.setHeader("Access-Control-Allow-Methods", "*")
        res.setHeader("Access-Control-Max-Age", "3600")
        res.setHeader(
            "Access-Control-Allow-Headers",
            "Origin, Content-Type, Accept, Authorization"
        )

        val requestURI = request.requestURI

        // 토큰 확인을 통과시킬 URI인지 확인
        if (!checkTokenPasser(requestURI)) {
            val token: String? = jwtTokenProvider.resolveToken(request)

            if (
                !token.isNullOrBlank() &&
                jwtTokenProvider.validateToken(token)
            ) {
                val authentication = jwtTokenProvider.getAuthentication(token)
                log.info("authentication : $authentication")
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        chain.doFilter(request, response)
    }

    private fun checkTokenPasser(requestURI: String): Boolean {
        return TOKEN_PASSER.any {
            if (it.startsWith("/")) requestURI.startsWith(it)
            else requestURI.contains(it)
        }
    }
}

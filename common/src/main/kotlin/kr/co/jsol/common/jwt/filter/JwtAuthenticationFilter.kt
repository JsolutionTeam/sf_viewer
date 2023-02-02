package kr.co.jsol.common.jwt.filter

import io.jsonwebtoken.io.IOException
import kr.co.jsol.common.jwt.JwtTokenProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(private val jwtTokenProvider: JwtTokenProvider) : GenericFilterBean() {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val res: HttpServletResponse = response as HttpServletResponse
        res.setHeader("Access-Control-Allow-Origin", "*")
        res.setHeader("Access-Control-Allow-Methods", "*")
        res.setHeader("Access-Control-Max-Age", "3600")
        res.setHeader(
            "Access-Control-Allow-Headers",
            "Origin, Content-Type, Accept, Authorization"
        )

        val token: String? = jwtTokenProvider.resolveToken((request as HttpServletRequest))
        logger.info("jwt auth token = $token")


//        logger.info("!token.isNullOrBlank() : ${!token.isNullOrBlank()}")
//        val validateToken = jwtTokenProvider.validateToken(token!!)
//        logger.info("jwtTokenProvider.validateToken(token) : $validateToken")

        if (!token.isNullOrBlank() && jwtTokenProvider.validateToken(token)) {
            val authentication = jwtTokenProvider.getAuthentication(token)
            logger.info("authentication = $authentication")
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain.doFilter(request, response)
    }
}

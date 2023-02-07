package kr.co.jsol.common.jwt.filter

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.jsol.common.exception.UnauthorizedException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

// https://stackoverflow.com/questions/34595605/how-to-manage-exceptions-thrown-in-filters-in-spring/55864206
@Component
class JwtExceptionFilter(private val objectMapper: ObjectMapper) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            // JwtFilter 를 호출하는데, 이 필터에서 jwtTokenNotAvailable 이 떨어진다.
            filterChain.doFilter(httpServletRequest, httpServletResponse)
        } catch (e: UnauthorizedException) {
            // https://stackoverflow.com/questions/57194249/how-to-return-response-as-json-from-spring-filter
            httpServletResponse.status = HttpStatus.UNAUTHORIZED.value()
            httpServletResponse.contentType = MediaType.APPLICATION_JSON_VALUE
            httpServletResponse.characterEncoding = "UTF-8"
            objectMapper.writeValue(httpServletResponse.writer, e.message)
        }
    }
}

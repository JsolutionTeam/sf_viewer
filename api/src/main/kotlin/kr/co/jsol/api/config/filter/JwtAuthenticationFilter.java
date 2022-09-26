package kr.co.jsol.api.config.filter;

import com.jsol.api.config.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j(topic = "logger")
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtTokenProvider) {
        this.jwtProvider = jwtTokenProvider;
    }

    //request들어오는 jwt의 유효성 검증 - JwtProvider.validationToken()을 필터로써 FilterChain에 추가
    @Override
    public void doFilter(ServletRequest request,
                        ServletResponse response,
                        FilterChain filterChain) throws IOException, ServletException {
        String token = jwtProvider.resolveToken((HttpServletRequest) request);
        String rfToken = jwtProvider.resolveRFToken((HttpServletRequest) request);
        if(jwtProvider.validateToken(token) || jwtProvider.validateToken(rfToken)){ // 둘 중에 하나라도 만료되지 않았다면 추가.
            Authentication authentication = jwtProvider.getAuthentication(token, rfToken);
            logger.info(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
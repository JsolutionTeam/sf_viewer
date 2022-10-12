package kr.co.jsol.api.config

import kr.co.jsol.api.jwt.JwtTokenProvider
import kr.co.jsol.api.jwt.filter.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.firewall.DefaultHttpFirewall
import org.springframework.security.web.firewall.HttpFirewall
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록. 필터체인(묶음)에 필터등록
@EnableGlobalMethodSecurity(
    securedEnabled = true, // Controller 에서 @Secured 어노테이션 사용가능. @Secured("roleName")
    prePostEnabled = true // preAuthorize 어노테이션 활성화
)
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
    private val corsFilter: CorsFilter,
) : WebSecurityConfigurerAdapter() {

    @Bean // 더블 슬래쉬 허용
    fun defaultHttpFirewall(): HttpFirewall {
        return DefaultHttpFirewall()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(http: HttpSecurity) {
        http.httpBasic().disable()
            .csrf().disable()
            .cors().disable()

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

//        http.authorizeRequests()
//            .antMatchers("/api/auth/login").permitAll()
//            .antMatchers("/api/auth/login").permitAll()
//            .antMatchers("/api/**").authenticated()

        http
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )

        http
            .addFilter(corsFilter)
    }

    //    override fun configure(http: HttpSecurity) {
//        http.csrf().disable()
//        http.cors().disable()
//        http
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않겠다.
//            .and()
//            .exceptionHandling()
//            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//            .and()
//            .addFilter(corsFilter) // 위의 addFilter를 하면 모든 요청은 corsFilter를 거치게 돼있음.
//            // @CrossOrigin는 인증이 필요한 상황에선 해결되지 않는다.
//            // 인증이 있을때는 시큐리티 필터에 등록을 해줘야 한다.
//            .addFilterBefore(JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter::class.java)
//            .formLogin().disable()
//            .headers()
//            .frameOptions().sameOrigin()
//            .and()
//            .httpBasic().disable()
//            .authorizeRequests()
//            .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
//            .antMatchers("/**").permitAll() // 권한 처리를 각 서비스에서 한다.
//            .anyRequest().authenticated()
//            .and()
//            .formLogin().disable()
//    }
//
    // ignore check swagger resource
    override fun configure(web: WebSecurity) {
        web.httpFirewall(defaultHttpFirewall())
        // 인증하지 않을 주소 추가.
        web.ignoring().antMatchers("/html/**", "/css/**", "/js/**", "/image/**")
        web.ignoring().antMatchers( // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**", // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**" // other public endpoints of your API may be appended to this array
        )
    }
}

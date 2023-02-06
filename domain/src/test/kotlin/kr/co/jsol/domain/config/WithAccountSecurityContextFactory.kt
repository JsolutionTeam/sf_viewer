package kr.co.jsol.domain.config

import kr.co.jsol.domain.entity.user.UserRepository
import kr.co.jsol.domain.entity.user.UserService
import kr.co.jsol.domain.entity.user.enums.UserRoleType
import kr.co.jsol.common.exception.entities.user.UserAlreadyExistException
import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.site.SiteRepository
import kr.co.jsol.domain.entity.user.User
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithAccountSecurityContextFactory(
    private val userRepository: UserRepository,
    private val userService: UserDetailsService,
    private val siteRepository: SiteRepository,
) : WithSecurityContextFactory<WithAccount> {
    override fun createSecurityContext(withAccount: WithAccount): SecurityContext {
        val nickname: String = withAccount.value

        val site001 = siteRepository.save(
            Site(
                id = 1L,
                name = "사이트001",
            )
        )

        try {
            userRepository.save(
                User(
                    id=nickname,
                    password = nickname,
                    role = UserRoleType.ROLE_ADMIN,
                    site = site001,
                )
            )
        } catch (ignore: UserAlreadyExistException) {}


        val principal: UserDetails = userService.loadUserByUsername(nickname)
        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            principal, principal.password, principal.authorities
        )
        val securityContext: SecurityContext = SecurityContextHolder.createEmptyContext()
        securityContext.authentication = authentication
        return securityContext
    }
}

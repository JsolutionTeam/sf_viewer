package kr.co.jsol.domain.entity.user

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import javax.security.auth.login.AccountException

@Service
class UserDetailServiceImpl(
    private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByIdAndLockedIsFalse(username) ?: throw AccountException("불가능한 계정입니다.")
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)

        fun getAccountFromSecurityContext(): User {
            val authentication = SecurityContextHolder.getContext().authentication
//            log.info("authentication : $authentication")
            val principal = authentication.principal
            log.info("principal : $principal")
            if (principal == "anonymousUser") throw Exception("계정이 확인되지 않습니다. 다시 로그인 해주세요.")
            return principal as User
        }
    }
}

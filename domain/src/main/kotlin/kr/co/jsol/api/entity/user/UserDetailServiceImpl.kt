package kr.co.jsol.api.entity.user

import kr.co.jsol.api.jwt.JwtTokenProvider
import kr.co.jsol.api.entity.util.findByIdOrThrow
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.security.auth.login.AccountException

@Service
class UserDetailServiceImpl(
    private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByIdAndLockedIsFalse(username) ?: throw AccountException("불가능한 계정입니다.")
    }
}

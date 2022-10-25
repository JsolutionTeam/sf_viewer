package kr.co.jsol.domain.entity.user

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
}

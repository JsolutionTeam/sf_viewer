package kr.co.jsol.domain.entity.user

import kr.co.jsol.common.jwt.JwtTokenProvider
import kr.co.jsol.common.jwt.dto.JwtToken
import kr.co.jsol.common.jwt.dto.RefreshTokenRequest
import kr.co.jsol.domain.entity.user.dto.request.LoginRequest
import kr.co.jsol.domain.entity.user.dto.response.LoginResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
) {

    private val log = LoggerFactory.getLogger(AuthService::class.java)

    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): JwtToken {
        val userPk = jwtTokenProvider.getUserPk(refreshTokenRequest.refreshToken)
        return JwtToken(jwtTokenProvider.createAccessToken(userPk), jwtTokenProvider.createRefreshToken(userPk))
    }

    fun login(loginRequest: LoginRequest): LoginResponse {
        val user: User = userRepository.findByIdAndLockedIsFalse(loginRequest.username)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "로그인 실패하셨습니다.")

        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    user.username,
                    loginRequest.password
                )
            )
        } catch (e: AuthenticationException) {
            log.error("[로그인 실패 사용자 정보] username : ${user.username}password : ${loginRequest.password}")
            e.printStackTrace()
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "로그인 실패하셨습니다.")
        } catch (e: LockedException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "계정이 잠겨 있습니다.")
        } catch (e: DisabledException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "계정이 비활성화 상태입니다.")
        } catch (e: CredentialsExpiredException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 만료 되었습니다.")
        } catch (e: AccountExpiredException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "계정이 만료되었습니다.")
        }

        return LoginResponse(
            role = user.role,
            siteSeq = user.site?.id ?: 0,
            accessToken = jwtTokenProvider.createAccessToken(user.username),
            refreshToken = jwtTokenProvider.createRefreshToken(user.username),
        )
    }
}

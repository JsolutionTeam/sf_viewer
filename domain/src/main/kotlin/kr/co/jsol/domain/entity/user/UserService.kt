package kr.co.jsol.domain.entity.user

import kr.co.jsol.common.exception.entities.user.UserAlreadyExistException
import kr.co.jsol.common.exception.entities.user.UserDisableException
import kr.co.jsol.common.jwt.JwtTokenProvider
import kr.co.jsol.common.jwt.dto.RefreshTokenDto
import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.site.SiteRepository
import kr.co.jsol.domain.entity.site.dto.response.SiteResponse
import kr.co.jsol.domain.entity.user.dto.request.LoginRequest
import kr.co.jsol.domain.entity.user.dto.request.UserRequest
import kr.co.jsol.domain.entity.user.dto.request.UserUpdateRequest
import kr.co.jsol.domain.entity.user.dto.response.LoginResponse
import kr.co.jsol.domain.entity.user.dto.response.UserResponse
import kr.co.jsol.domain.entity.user.enums.UserRoleType
import kr.co.jsol.domain.entity.util.findByIdOrThrow
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.*
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val userQuerydslRepository: UserQuerydslRepository,
    private val siteRepository: SiteRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    private val log = LoggerFactory.getLogger(UserService::class.java)

    fun refreshToken(userId: String): RefreshTokenDto {
        return RefreshTokenDto(jwtTokenProvider.createRefreshToken(userId))
    }

    fun login(loginRequest: LoginRequest): LoginResponse {
        val user: User = userRepository.findByIdAndLockedIsFalse(loginRequest.username)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "로그인 실패하셨습니다.")

        try {
            log.info("username : ${user.username}\npassword : ${loginRequest.password}")
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    user.username,
                    loginRequest.password
                )
            )
        } catch (e: AuthenticationException) {
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
            siteSeq = user.site!!.id,
            accessToken = jwtTokenProvider.createAccessToken(user.username),
            refreshToken = jwtTokenProvider.createRefreshToken(user.username),
        )
    }

    fun isExistUserById(id: String): Boolean {
        return userRepository.existsById(id)
    }

    fun getById(id: String): User {
        return userRepository.findByIdOrThrow(id, "계정 정보를 찾을 수 없습니다.")
    }

    fun getAll(): List<UserResponse> {
        return userQuerydslRepository.findAllBy()
    }

    fun createUser(userRequest: UserRequest): UserResponse {

        val encodePw = passwordEncoder.encode(userRequest.password)
        userRequest.setEncryptPassword(encodePw)

        val user: User = userRequest.toEntity()

        try {
            val existUser = userRepository.findByIdAndLockedIsFalse(user.username)
            if (existUser != null) throw UserAlreadyExistException()
        } catch (_: Exception) {
        }

        val site = siteRepository.findByIdOrThrow(userRequest.siteSeq, "농장 정보를 다시 확인해주세요.")

        user.updateInfo(site = site)
        val saveUser = userRepository.save(user)

        return UserResponse(
            username = saveUser.username,
            role = saveUser.role,
            site = SiteResponse.of(site),
        )
    }

    fun updateUser(userUpdateRequest: UserUpdateRequest): UserResponse {
        val user = userRepository.findByIdAndLockedIsFalse(userUpdateRequest.username)
            ?: throw UserDisableException()

        val site: Site? = if (userUpdateRequest.siteSeq != null) {
            siteRepository.findByIdOrNull(userUpdateRequest.siteSeq)
        } else {
            null
        }
        val role: UserRoleType? = userUpdateRequest.role
        val password = userUpdateRequest.password ?: ""


        user.updateInfo(
            role = role,
            site = site,
        )

        if(password.isNotBlank()){
            val newPassword = passwordEncoder.encode(password)
            user.updatePassword(newPassword)
        }

        val updateUser = userRepository.save(user)

        return UserResponse.of(updateUser)
    }

    fun deleteUserById(id: String): Boolean {
//        val user = userRepository.findByIdAndLockedIsFalse(id)
//            ?: throw UserDisableException()
//
//        user.updateInfo(locked = true)
//        userRepository.save(user)
        userRepository.deleteById(id)
        return true
    }
}

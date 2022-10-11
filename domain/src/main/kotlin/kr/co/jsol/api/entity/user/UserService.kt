package kr.co.jsol.api.entity.user

import kr.co.jsol.api.entity.site.Site
import kr.co.jsol.api.entity.site.SiteRepository
import kr.co.jsol.api.entity.user.dto.request.LoginRequest
import kr.co.jsol.api.entity.user.dto.request.UserRequest
import kr.co.jsol.api.entity.user.dto.request.UserUpdateRequest
import kr.co.jsol.api.entity.user.dto.response.LoginResponse
import kr.co.jsol.api.entity.user.dto.response.UserResponse
import kr.co.jsol.api.jwt.JwtTokenProvider
import kr.co.jsol.api.entity.util.findByIdOrThrow
import kr.co.jsol.api.exception.entities.user.UserAlreadyExistUserException
import kr.co.jsol.api.exception.entities.user.UserDisableException
import kr.co.jsol.api.jwt.dto.RefreshTokenDto
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val siteRepository: SiteRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun refreshToken(userId: String): RefreshTokenDto {
        return RefreshTokenDto(jwtTokenProvider.createRefreshToken(userId))
    }

    fun login(loginRequest: LoginRequest): LoginResponse {
        val user: User = userRepository.findByIdAndLockedIsFalse(loginRequest.username)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "로그인 실패하셨습니다.")

        try {
            println("username : ${user.username}\npassword : ${loginRequest.password}")
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

    @Transactional(readOnly = true)
    fun getById(id: String): User {
        return userRepository.findByIdOrThrow(id, "계정 정보를 찾을 수 없습니다.")
    }

    @Transactional(readOnly = true)
    fun getAll(): List<User> {
        return userRepository.findAll(Sort.by("name").ascending())
    }

    fun createUser(userRequest: UserRequest): UserResponse {

        val encodePw = passwordEncoder.encode(userRequest.password)
        userRequest.setEncryptPassword(encodePw)

        val user: User = userRequest.toEntity()

        try {
            val existUser = userRepository.findByIdAndLockedIsFalse(user.username)
            if (existUser != null) throw UserAlreadyExistUserException()
        } catch (_: Exception) {
        }

        val site = siteRepository.findByIdOrThrow(userRequest.siteSeq, "농장 정보를 다시 확인해주세요.")

        user.updateInfo(site = site)
        val saveUser = userRepository.save(user)

        return UserResponse(
            username = saveUser.username,
            role = saveUser.role,
            site = site
        )
    }

    fun updateUser(userUpdateRequest: UserUpdateRequest): UserResponse {
        val user = userRepository.findByIdAndLockedIsFalse(userUpdateRequest.username)
            ?: throw UserDisableException()

        val site: Site? = siteRepository.findByIdOrNull(userUpdateRequest.siteSeq) ?: user.site
        val role = userUpdateRequest.role ?: user.role

        user.updateInfo(
            site = site,
            role = role
        )

        val updateUser = userRepository.save(user)

        return UserResponse(
            username = updateUser.username,
            role = updateUser.role,
            site = updateUser.site,
        )
    }
}

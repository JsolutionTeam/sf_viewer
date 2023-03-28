package kr.co.jsol.domain.entity.user

import kr.co.jsol.common.exception.entities.user.UserAlreadyExistException
import kr.co.jsol.common.exception.entities.user.UserDisableException
import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.site.SiteRepository
import kr.co.jsol.domain.entity.site.dto.response.SiteResponse
import kr.co.jsol.domain.entity.user.dto.request.UserRequest
import kr.co.jsol.domain.entity.user.dto.request.UserUpdateRequest
import kr.co.jsol.domain.entity.user.dto.response.UserResponse
import kr.co.jsol.domain.entity.user.enums.UserRoleType
import kr.co.jsol.domain.entity.util.findByIdOrThrow
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userQuerydslRepository: UserQuerydslRepository,
    private val siteRepository: SiteRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    private val log = LoggerFactory.getLogger(UserService::class.java)

    fun isExistUserById(id: String): Boolean {
        return userRepository.existsById(id)
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

        if (password.isNotBlank()) {
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

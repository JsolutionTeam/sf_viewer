package kr.co.jsol.domain.entity.user

import kr.co.jsol.common.exception.entities.user.UserAlreadyExistException
import kr.co.jsol.common.exception.entities.user.UserDisableException
import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.site.SiteRepository
import kr.co.jsol.domain.entity.user.dto.request.UserRequest
import kr.co.jsol.domain.entity.user.dto.request.UserUpdateRequest
import kr.co.jsol.domain.entity.user.dto.response.UserResponse
import kr.co.jsol.domain.entity.user.enums.UserRoleType
import org.slf4j.LoggerFactory
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
        return userQuerydslRepository.findAllBy().map { UserResponse(it) }
    }

    fun createUser(userRequest: UserRequest): UserResponse {
        val existUser = userRepository.findByIdAndLockedIsFalse(userRequest.username)
        if (existUser != null) throw UserAlreadyExistException()

        val site = siteRepository.save(Site(crop = userRequest.crop, location = userRequest.location))

        userRequest.setEncryptPassword(passwordEncoder.encode(userRequest.password))
        val user: User = userRequest.toEntity()
        user.site = site

        userRepository.save(user)

        return UserResponse(user)
    }

    fun updateUser(userUpdateRequest: UserUpdateRequest): UserResponse {
        val user = userRepository.findByIdAndLockedIsFalse(userUpdateRequest.username)
            ?: throw UserDisableException()

        val role: UserRoleType? = userUpdateRequest.role
        val password = userUpdateRequest.password ?: ""

        user.updateInfo(role = role)

        if (password.isNotBlank()) {
            val newPassword = passwordEncoder.encode(password)
            user.updatePassword(newPassword)
        }
        if (user.site != null) {
            user.site!!.update(userUpdateRequest.crop, userUpdateRequest.location)
        }

        val updateUser = userRepository.save(user)

        return UserResponse(updateUser)
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

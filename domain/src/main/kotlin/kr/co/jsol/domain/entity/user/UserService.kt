package kr.co.jsol.domain.entity.user

import kr.co.jsol.common.exception.entities.user.UserAlreadyExistException
import kr.co.jsol.common.exception.entities.user.UserDisableException
import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.site.SiteRepository
import kr.co.jsol.domain.entity.user.dto.request.UserRequest
import kr.co.jsol.domain.entity.user.dto.request.UserUpdateRequest
import kr.co.jsol.domain.entity.user.dto.response.UserResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

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

    fun getAllUser(): List<UserResponse> {
        return userQuerydslRepository.findAllBy().map { UserResponse(it) }
    }

    fun getUser(id: String): UserResponse {
        val user = userRepository.findByIdAndLockedIsFalse(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return UserResponse(user)
    }

    fun createUser(userRequest: UserRequest): UserResponse {
        val existUser = userRepository.findByIdAndLockedIsFalse(userRequest.username)
        if (existUser != null) throw UserAlreadyExistException()

        val site = Site(name = userRequest.name, crop = userRequest.crop, location = userRequest.location)
        siteRepository.save(site)

        userRequest.setEncryptPassword(passwordEncoder.encode(userRequest.password))
        val user: User = userRequest.toEntity()
        user.site = site

        userRepository.save(user)

        return UserResponse(user)
    }

    fun updateUser(userUpdateRequest: UserUpdateRequest): UserResponse {
        val user = userRepository.findByIdAndLockedIsFalse(userUpdateRequest.username)
            ?: throw UserDisableException()

        user.updateInfo(
            password = if (userUpdateRequest.password != null) passwordEncoder.encode(userUpdateRequest.password) else null,
            role = userUpdateRequest.role,
            email = userUpdateRequest.email,
            phone = userUpdateRequest.phone,
            address = userUpdateRequest.address
        )
        if (user.site != null) {
            user.site!!.update(userUpdateRequest.crop, userUpdateRequest.location)
        }

        userRepository.save(user)

        return UserResponse(user)
    }

    fun deleteUserById(id: String): Boolean {
        userRepository.deleteById(id)
        return true
    }
}

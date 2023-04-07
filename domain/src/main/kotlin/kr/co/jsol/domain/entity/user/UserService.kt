package kr.co.jsol.domain.entity.user

import kr.co.jsol.common.exception.entities.user.UserAlreadyExistException
import kr.co.jsol.common.exception.entities.user.UserDisableException
import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.site.SiteRepository
import kr.co.jsol.domain.entity.user.dto.request.UserRequest
import kr.co.jsol.domain.entity.user.dto.request.UserSearchCondition
import kr.co.jsol.domain.entity.user.dto.request.UserUpdateRequest
import kr.co.jsol.domain.entity.user.dto.response.UserResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

    fun getUsers(userSearchCondition: UserSearchCondition): List<UserResponse> {
        return userQuerydslRepository.getUsers(userSearchCondition).map { UserResponse(it) }
    }

    fun getUser(id: String): UserResponse {
        val user = userRepository.findByIdAndLockedIsFalse(id) ?: throw UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.")
        return UserResponse(user)
    }

    fun createUser(userRequest: UserRequest): UserResponse {
        val existUser = userRepository.findByIdAndLockedIsFalse(userRequest.username)
        if (existUser != null) throw UserAlreadyExistException()

        val site = Site(
            name = userRequest.siteName,
            crop = userRequest.siteCrop,
            location = userRequest.siteLocation
        )
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

    @Transactional
    fun deleteUserById(id: String): Boolean {
        val user: User = userRepository.findById(id).orElseThrow { UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.") }
        user.site?.let { siteRepository.delete(it) }
        userRepository.delete(user)
        return true
    }
}

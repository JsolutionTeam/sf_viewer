package kr.co.jsol.domain.entity.user

import kr.co.jsol.common.exception.ForbiddenException
import kr.co.jsol.common.exception.entities.user.UserAlreadyExistException
import kr.co.jsol.common.exception.entities.user.UserDisableException
import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.site.SiteRepository
import kr.co.jsol.domain.entity.user.dto.request.UserRequest
import kr.co.jsol.domain.entity.user.dto.request.UserSearchCondition
import kr.co.jsol.domain.entity.user.dto.request.UserUpdateRequest
import kr.co.jsol.domain.entity.user.dto.response.UserResponse
import kr.co.jsol.domain.entity.user.enums.UserRoleType
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

    @Transactional
    fun createUser(userRequest: UserRequest): UserResponse {
        val existUser = userRepository.findByIdAndLockedIsFalse(userRequest.username)
        if (existUser != null) throw UserAlreadyExistException()

        userRequest.setEncryptPassword(passwordEncoder.encode(userRequest.password))
        val user: User = userRequest.toEntity()
        userRepository.save(user)

        ///////////////////////////////////////////////////////////

        // 관리자 권한이면 농가를 지정하지 않는다.
        if(userRequest.role == UserRoleType.ROLE_USER) {
            // 입력받은 농가번호로 조회
            val siteSeq = userRequest.siteSeq
            var site = siteRepository.findById(siteSeq).orElse(null)

            // 농가번호가 중복된다면 저장하지 않는다.(예외처리)
            if (site != null) {
                throw ForbiddenException()
            }

            // 농가번호가 중복되지 않는다면 새로 등록한다.
            site = Site(
                id = siteSeq,
                name = userRequest.siteName,
                crop = userRequest.siteCrop,
                location = userRequest.siteLocation,
                delay = userRequest.siteDelay,
                apiKey = userRequest.siteApiKey,
            )
            siteRepository.save(site)

            // 사용자 정보에 농가정보를 저장한다.
            user.site = site
            userRepository.save(user)
        }

        return UserResponse(user)
    }

    @Transactional
    fun updateUser(userUpdateRequest: UserUpdateRequest): UserResponse {
        val user = userRepository.findByIdAndLockedIsFalse(userUpdateRequest.username)
            ?: throw UserDisableException()

        user.updateInfo(
            password = if (userUpdateRequest.password != null) passwordEncoder.encode(userUpdateRequest.password) else null,
            name = userUpdateRequest.name,
            role = userUpdateRequest.role,
            email = userUpdateRequest.email,
            phone = userUpdateRequest.phone,
            address = userUpdateRequest.address
        )

        // 사용자의 농가정보가 존재한다면 농가정보도 수정한다.
        if (user.site != null) {
            user.site!!.update(
                name = userUpdateRequest.siteName,
                crop = userUpdateRequest.siteCrop,
                location = userUpdateRequest.siteLocation,
                delay = userUpdateRequest.siteDelay,
                apiKey = userUpdateRequest.siteApiKey,
            )
        }

        userRepository.save(user)

        return UserResponse(user)
    }

    @Transactional
    fun deleteUserById(id: String): Boolean {
        val user: User = userRepository.findById(id).orElseThrow { UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.") }

        // 사용자일 시에만 농가정보를 삭제한다.
        if (user.role == UserRoleType.ROLE_USER) {
            user.site?.let { siteRepository.delete(it) }
        }
        userRepository.delete(user)
        return true
    }
}

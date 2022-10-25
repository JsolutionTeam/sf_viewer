package kr.co.jsol.domain.entity.user.dto.request

import kr.co.jsol.domain.entity.user.User
import kr.co.jsol.domain.entity.user.enum.UserRoleType

data class UserUpdateRequest(
    val username: String,
    val role: UserRoleType,
    val siteSeq: Long,
) {

    fun toEntity(): User {
        return User(
            id = username,
            password = "",
            role = role,
        )
    }
}

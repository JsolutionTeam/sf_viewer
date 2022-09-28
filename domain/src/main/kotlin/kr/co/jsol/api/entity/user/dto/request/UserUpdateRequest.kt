package kr.co.jsol.api.entity.user.dto.request

import kr.co.jsol.api.entity.user.User
import kr.co.jsol.api.entity.user.enum.UserRoleType

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

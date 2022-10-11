package kr.co.jsol.api.entity.user.dto.request

import kr.co.jsol.api.entity.user.User
import kr.co.jsol.api.entity.user.enum.UserRoleType

data class UserRequest(
    val username: String,
    var password: String,
    val role: UserRoleType,
    val siteSeq: Long,
) {

    fun setEncryptPassword(encodePw: String) {
        this.password = encodePw
    }

    fun toEntity(): User {
        return User(
            id = username,
            password = password,
            role = role,
        )
    }
}

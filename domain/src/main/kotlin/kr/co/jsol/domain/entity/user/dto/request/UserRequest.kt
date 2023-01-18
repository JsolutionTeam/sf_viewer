package kr.co.jsol.domain.entity.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.jsol.domain.entity.user.User
import kr.co.jsol.domain.entity.user.enum.UserRoleType

data class UserRequest(
    val username: String,
    var password: String,
    val role: UserRoleType,
    val siteSeq: Long,
) {

    @Schema(hidden = true)
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

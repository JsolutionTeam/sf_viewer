package kr.co.jsol.api.entity.user.dto.response

import kr.co.jsol.api.entity.site.Site
import kr.co.jsol.api.entity.user.enum.UserRoleType

data class UserResponse(
    val username: String,
    val role: UserRoleType,
    val site: Site?
)

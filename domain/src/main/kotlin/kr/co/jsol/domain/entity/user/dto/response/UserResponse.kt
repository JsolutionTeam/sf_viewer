package kr.co.jsol.domain.entity.user.dto.response

import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.user.enum.UserRoleType

data class UserResponse(
    val username: String,
    val role: UserRoleType,
    val site: Site?
)

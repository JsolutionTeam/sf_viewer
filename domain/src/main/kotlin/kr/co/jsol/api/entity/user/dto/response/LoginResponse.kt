package kr.co.jsol.api.entity.user.dto.response

import kr.co.jsol.api.entity.user.enum.UserRoleType

data class LoginResponse(
    val role: UserRoleType,
    val siteSeq: Long,
    val accessToken: String,
    val refreshToken: String,
)

package kr.co.jsol.domain.entity.user.dto.response

import kr.co.jsol.domain.entity.user.enums.UserRoleType

data class LoginResponse(
    val role: UserRoleType,
    val siteSeq: Long,
    val accessToken: String,
    val refreshToken: String,
)

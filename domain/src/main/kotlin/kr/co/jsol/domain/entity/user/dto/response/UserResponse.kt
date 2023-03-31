package kr.co.jsol.domain.entity.user.dto.response

import kr.co.jsol.domain.entity.site.dto.response.SiteResponse
import kr.co.jsol.domain.entity.user.User
import kr.co.jsol.domain.entity.user.enums.UserRoleType

data class UserResponse(
    val username: String,
    val role: UserRoleType,
    val email: String,
    val phone: String,
    val address: String,
    val site: SiteResponse,
) {
    constructor(user: User) : this(
        username = user.username,
        role = user.role,
        email = user.email,
        phone = user.phone,
        address = user.address,
        site = SiteResponse(user.site!!),
    )
}

package kr.co.jsol.domain.entity.user.dto.response

import kr.co.jsol.domain.entity.site.dto.response.SiteResponse
import kr.co.jsol.domain.entity.user.User
import kr.co.jsol.domain.entity.user.enums.UserRoleType

data class UserResponse(
    val name: String,
    val username: String,
    val role: UserRoleType,
    val email: String,
    val phone: String,
    val address: String,
    val site: SiteResponse?,
) {
    constructor(user: User) : this(
        name = user.name,
        username = user.username,
        role = user.role,
        email = user.email,
        phone = user.phone,
        address = user.address,
        //TODO 사용자가 많아지면 속도가 느려질 수 있음. 추후 수정 필요
        site = user.site?.let { SiteResponse(it) },
    )
}

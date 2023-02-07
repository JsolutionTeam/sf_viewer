package kr.co.jsol.domain.entity.user.dto.response

import kr.co.jsol.domain.entity.site.dto.response.SiteResponse
import kr.co.jsol.domain.entity.user.User
import kr.co.jsol.domain.entity.user.enums.UserRoleType

data class UserResponse(
    val username: String,
    val role: UserRoleType,
    val site: SiteResponse,
) {
    constructor(
        username: String,
        role: UserRoleType,
        siteId: Long,
        siteName: String,
    ) : this(
        username = username,
        role = role,
        site = SiteResponse(
            id = siteId,
            name = siteName,
        ),
    )

    companion object {
        fun of(user: User): UserResponse {
            return UserResponse(
                username = user.username,
                role = user.role,
                site = SiteResponse.of(user.site!!),
            )
        }
    }
}

package kr.co.jsol.domain.entity.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.jsol.common.util.ValidEnum
import kr.co.jsol.domain.entity.user.User
import kr.co.jsol.domain.entity.user.enums.UserRoleType
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class UserRequest(
    @NotBlank(message = "사용자 아이디는 필수 입력입니다.")
    val username: String,
    @NotBlank(message = "사용자 비밀번호는 필수 입력입니다.")
    var password: String,
    @Schema(description = "사용자 권한", required = true)
    @ValidEnum(enumClass = UserRoleType::class, message = "사용자 권한은 필수 입력입니다.")
    val role: UserRoleType,
    @Schema(description = "사용자 농장 번호", required = true)
    @Min(value = 1, message = "사용자 농장 번호는 1 이상의 숫자여야 합니다.")
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

package kr.co.jsol.domain.entity.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.jsol.common.util.ValidEnum
import kr.co.jsol.domain.entity.user.User
import kr.co.jsol.domain.entity.user.enums.UserRoleType
import javax.validation.constraints.NotBlank

data class UserRequest(
    @NotBlank(message = "사용자 아이디는 필수 입력입니다.")
    val username: String,
    @NotBlank(message = "사용자 이름은 필수 입력입니다.")
    val name: String,
    @NotBlank(message = "사용자 비밀번호는 필수 입력입니다.")
    var password: String,
    @Schema(description = "사용자 권한", required = true)
    @ValidEnum(enumClass = UserRoleType::class, message = "사용자 권한은 필수 입력입니다.")
    val role: UserRoleType,
    @NotBlank(message = "사용자 이메일는 필수 입력입니다.")
    val email: String,
    @NotBlank(message = "사용자 전화번호는 필수 입력입니다.")
    val phone: String,
    @NotBlank(message = "사용자 주소는 필수 입력입니다.")
    val address: String,
    @NotBlank(message = "사용자 농장 이름은 필수 입력입니다.")
    val siteName: String,
    @NotBlank(message = "사용자 농장 작물은 필수 입력입니다.")
    val siteCrop: String,
    @NotBlank(message = "사용자 농장 지역은 필수 입력입니다.")
    val siteLocation: String,
) {

    @Schema(hidden = true)
    fun setEncryptPassword(encodePw: String) {
        this.password = encodePw
    }

    fun toEntity(): User {
        return User(
            id = username,
            name = name,
            password = password,
            role = role,
            email = email,
            phone = phone,
            address = address,
        )
    }
}

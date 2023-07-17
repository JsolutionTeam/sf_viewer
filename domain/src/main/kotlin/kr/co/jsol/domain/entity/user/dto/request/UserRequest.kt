package kr.co.jsol.domain.entity.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.jsol.common.util.ValidEnum
import kr.co.jsol.domain.entity.user.User
import kr.co.jsol.domain.entity.user.enums.UserRoleType
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Min
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

    val email: String,
    val phone: String,
    val address: String,

    // 추가 설정 정보
    @Schema(description = "농가 번호")
    @Length(min = 1, max = 19, message = "농가 번호는 1~19자리수까지 입력 가능합니다.")
    @Min(value = 1, message = "농가 번호는 1 이상의 숫자만 입력 가능합니다.")
    val siteSeq: Long,
    @NotBlank(message = "사용자 농장 이름은 필수 입력입니다.")
    val siteName: String,
    @NotBlank(message = "사용자 농장 작물은 필수 입력입니다.")
    val siteCrop: String,
    @NotBlank(message = "사용자 농장 지역은 필수 입력입니다.")
    val siteLocation: String,

    @Schema(description = "센서 데이터 전송 주기")
    @NotBlank(message = "센서 데이터 전송 주기는 필수 입력입니다.")
    val siteDelay: Long,

    // 농진청 API 관련
    @Schema(description = "사용자 농진청 APIKEY")
    @NotBlank(message = "사용자 농진청 APIKEY는 필수 입력입니다.")
    val siteApiKey: String,
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

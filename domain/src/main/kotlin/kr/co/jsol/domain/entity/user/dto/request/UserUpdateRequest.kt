package kr.co.jsol.domain.entity.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.jsol.domain.entity.user.enums.UserRoleType

@Schema(description = "사용자 정보 수정 요청 DTO")
data class UserUpdateRequest(
    // 사용자 기본 정보
    @Schema(description = "대상 사용자 아이디", required = true)
    val username: String,
    @Schema(description = "변경할 사용자 비밀번호", required = false)
    val password: String?,
    @Schema(description = "변경할 사용자 이메일", required = false)
    val email: String?,
    @Schema(description = "변경할 사용자 전화번호", required = false)
    val phone: String?,
    @Schema(description = "변경할 사용자 주소", required = false)
    val address: String?,
    @Schema(description = "변경할 사용자 권한", required = false)
    val role: UserRoleType?,

    // 추가 설정 정보
    @Schema(description = "변경할 농가 이름", required = false)
    val siteName: String?,
    @Schema(description = "변경할 농가 작물", required = false)
    val siteCrop: String?,
    @Schema(description = "변경할 농가 지역", required = false)
    val siteLocation: String?,
    @Schema(description = "변경할 농가 센서 데이터 통신 지연시간", required = false)
    val siteDelay: Long?,
    @Schema(description = "변경할 농가 농촌진흥청 API KEY ", required = false)
    val siteApiKey: String?,
)

package kr.co.jsol.domain.entity.rdacolumn.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank

data class RdaColumnCreateRequest(
    @Schema(description = "수집 항목 영문명", example = "earthHumidity")
    @NotBlank(message = "수집 항목 영문명은 필수 입력값입니다.")
    val eng: String,

    @Schema(description = "수집 항목 한글명", example = "대지 습도")
    @NotBlank(message = "수집 항목 한글명은 필수 입력값입니다.")
    val kor: String,
)

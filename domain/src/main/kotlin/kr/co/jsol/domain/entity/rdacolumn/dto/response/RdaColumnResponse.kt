package kr.co.jsol.domain.entity.rdacolumn.dto.response

import io.swagger.v3.oas.annotations.media.Schema

data class RdaColumnResponse(
    @Schema(description = "수집 항목 영문명", example = "earthHumidity")
    val eng: String,

    @Schema(description = "수집 항목 한글명", example = "대지 습도")
    val kor: String,
)

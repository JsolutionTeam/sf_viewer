package kr.co.jsol.domain.entity.rdacolumn.dto.response

import io.swagger.v3.oas.annotations.media.Schema

data class RdaColumnWithDataResponse(
    @Schema(description = "수집 항목 영문명", example = "earthHumidity")
    val eng: String,

    @Schema(description = "수집된 데이터" , example = "1.1, 2023-01-01 12:34:56")
    val data: Any,
)

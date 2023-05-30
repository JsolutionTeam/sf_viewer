package kr.co.jsol.domain.entity.rdacolumn.dto.response

import io.swagger.v3.oas.annotations.media.Schema

data class RdaColumnResponse(
    @Schema(description = "수집 항목 데이터베이스에서 사용하는 컬럼명", example = "temperature(대기온도를 의미)")
    val property: String,

    @Schema(description = "수집 항목 API 송신 시 항목 값 구분하는 키값", example = "tp_extrl(온도_외부를 의미)")
    val key: String,

    @Schema(description = "수집 항목 한글명 혹은 설명")
    var description: String,

    @Schema(description = "조회 순서 -> 데이터 입력 순서를 맞추기 위해 사용")
    var no: Int,
)

package kr.co.jsol.domain.entity.sensorDevice.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.jsol.domain.common.BaseCondition

@Schema(description = "발주 검색 조건 laundry 권한이라면 단일 clientId, departmentId 조건 처리 가능, client 권한이라면 단일 department 조건 처리 가능.")
data class SensorDeviceSearchCondition(
    @Schema(description = "해당 type를 포함하는 값 검색, 빈 값 가능하며 빈 값을 넣으면 필터링하지 않음")
    val type: String?,

    @Schema(description = "해당 modelName를 포함하는 값 검색, 빈 값 가능하며 빈 값을 넣으면 필터링하지 않음")
    val modelName: String?,

    @Schema(description = "해당 serialNumber를 포함하는 값 검색, 빈 값 가능하며 빈 값을 넣으면 필터링하지 않음")
    val serialNumber: String?,

    @Schema(description = "해당 ip를 포함하는 값 검색, 빈 값 가능하며 빈 값을 넣으면 필터링하지 않음")
    val ip: String?,

    @Schema(description = "해당 memo를 포함하는 값 검색, 빈 값 가능하며 빈 값을 넣으면 필터링하지 않음")
    val memo: String?,

    @Schema(description = "해당 siteSeq 데이터만 검색, 빈 값 가능하며 빈 값을 넣으면 필터링하지 않음")
    val siteSeq: Long?,
) : BaseCondition()

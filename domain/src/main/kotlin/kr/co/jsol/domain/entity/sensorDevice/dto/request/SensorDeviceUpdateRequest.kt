package kr.co.jsol.domain.entity.sensorDevice.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.validator.constraints.Length

data class SensorDeviceUpdateRequest(
    @Length(max = 20, message = "type은 20자 이하로 입력해주세요.")
    @Schema(description = "센서 타입, 최대 20자 입니다.", example = "강우")
    val type: String?,

    @Length(max = 10, message = "unit은 10자 이하로 입력해주세요.")
    @Schema(description = "데이터 단위, 최대 10자 입니다.", example = "mm")
    val unit: String?,

    @Length(max = 30, message = "modelName은 30자 이하로 입력해주세요.")
    @Schema(description = "기기 모델 명, 최대 30자 입니다.", example = "ABC-1234")
    val modelName: String?,

    @Length(max = 30, message = "serialNumber는 255자 이하로 입력해주세요.")
    @Schema(description = "기기 일련번호, 최대 255자 입니다.", example = "1234ABCD")
    val serialNumber: String?,

    val ip: String?,

    @Length(max = 1000, message = "메모는 1000자 이하로 입력해주세요.")
    val memo: String?,

    @Length(min = 1, message = "농가 번호를 제대로 입력해주세요.")
    val siteSeq: Long?,
)

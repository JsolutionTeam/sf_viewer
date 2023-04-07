package kr.co.jsol.domain.entity.sensorDevice.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.jsol.domain.entity.sensorDevice.SensorDevice
import kr.co.jsol.domain.entity.site.Site
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank

data class SensorDeviceCreateRequest(
    @NotBlank(message = "type은 필수값입니다.")
    @Length(max = 20, message = "type은 20자 이하로 입력해주세요.")
    @Schema(description = "센서 타입, 최대 20자 입니다.", example = "강우")
    val type: String?,

    @Length(max = 10, message = "unit은 10자 이하로 입력해주세요.")
    @Schema(description = "데이터 단위, 최대 10자 입니다.", example = "mm")
    val unit: String?,

    @NotBlank(message = "modelName는 필수값입니다.")
    @Length(max = 30, message = "modelName은 30자 이하로 입력해주세요.")
    @Schema(description = "기기 모델 명, 최대 30자 입니다.", example = "ABC-1234")
    val modelName: String?,

    @NotBlank(message = "serialNumber는 필수값입니다.")
    @Length(max = 255, message = "serialNumber는 255자 이하로 입력해주세요.")
    @Schema(description = "기기 일련번호, 최대 255자 입니다.", example = "1234ABCD")
    val serialNumber: String?,

    @NotBlank(message = "ip는 필수값입니다.")
    @Length(max = 50, message = "ip는 50자 이하로 입력해주세요.")
    val ip: String?,

    @Length(max = 1000, message = "메모는 1000자 이하로 입력해주세요.")
    val memo: String?,

    @Length(min = 1, message = "농가 번호를 제대로 입력해주세요.")
    val siteSeq: Long?,
) {
    fun toEntity(site: Site): SensorDevice {
        return SensorDevice(
            type = type!!,
            unit = unit ?: "",
            modelName = modelName!!,
            serialNumber = serialNumber!!,
            ip = ip!!,
            memo = memo!!,
            site = site,
        )
    }
}

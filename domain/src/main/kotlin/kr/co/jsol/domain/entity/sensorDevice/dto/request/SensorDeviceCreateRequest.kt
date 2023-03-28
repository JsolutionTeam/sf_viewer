package kr.co.jsol.domain.entity.sensorDevice.dto.request

import kr.co.jsol.domain.entity.sensorDevice.SensorDevice
import kr.co.jsol.domain.entity.site.Site
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank

data class SensorDeviceCreateRequest(
    @NotBlank(message = "deviceNo는 필수값입니다.")
    val deviceNo: String?,

    @NotBlank(message = "serialNumber는 필수값입니다.")
    val serialNumber: String?,

    @NotBlank(message = "ip는 필수값입니다.")
    val ip: String?,

    @Length(max = 1000, message = "메모는 1000자 이하로 입력해주세요.")
    val memo: String?,

    @Length(min = 1, message = "농가 번호를 제대로 입력해주세요.")
    val siteSeq: Long?,
) {
    fun toEntity(site: Site): SensorDevice {
        return SensorDevice(
            deviceNo = deviceNo!!,
            serialNumber = serialNumber!!,
            ip = ip!!,
            memo = memo!!,
            site = site,
        )
    }
}

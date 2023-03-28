package kr.co.jsol.domain.entity.sensorDevice.dto.request

import kr.co.jsol.common.util.parseDouble
import kr.co.jsol.common.util.parseLong
import kr.co.jsol.domain.entity.sensor.Sensor
import kr.co.jsol.domain.entity.sensor.enums.SensorProperty
import kr.co.jsol.domain.entity.sensorDevice.SensorDevice
import kr.co.jsol.domain.entity.site.Site
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank

data class SensorDeviceUpdateRequest(
    @Length(max=50, message = "ip는 50자 이내로 입력해주세요.")
    val ip: String?,

    @Length(max = 1000, message = "메모는 1000자 이하로 입력해주세요.")
    val memo: String?,

    @Length(min = 1, max=Int.MAX_VALUE, message = "농가 번호를 제대로 입력해주세요.")
    val siteSeq: Long?,
)

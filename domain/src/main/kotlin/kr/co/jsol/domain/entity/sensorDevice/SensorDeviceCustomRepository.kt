package kr.co.jsol.domain.entity.sensorDevice

import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceSearchCondition
import kr.co.jsol.domain.entity.sensorDevice.dto.response.SensorDeviceResponse

interface SensorDeviceCustomRepository {
    fun getSensorDeviceList(siteSearchCondition: SensorDeviceSearchCondition?): List<SensorDeviceResponse>

}

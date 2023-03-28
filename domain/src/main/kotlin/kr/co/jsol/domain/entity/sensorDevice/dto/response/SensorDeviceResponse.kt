package kr.co.jsol.domain.entity.sensorDevice.dto.response

import com.querydsl.core.annotations.QueryProjection
import kr.co.jsol.domain.entity.sensorDevice.SensorDevice

data class SensorDeviceResponse @QueryProjection constructor(
    val sensorDeviceId: Long,
    val deviceNo: String,
    val serialNumber: String,
    val ip: String,
    val memo: String,
    val siteSeq: Long,
) {
    companion object {
        fun of(
            sensorDevice: SensorDevice
        ): SensorDeviceResponse {
            return of(sensorDevice.id!!, sensorDevice.deviceNo, sensorDevice.serialNumber, sensorDevice.ip, sensorDevice.memo, sensorDevice.site.id!!)
        }

        fun of(sensorDeviceId: Long, deviceNo: String, serialNumber: String, ip: String, memo: String, siteSeq: Long): SensorDeviceResponse {
            return SensorDeviceResponse(sensorDeviceId, deviceNo, serialNumber, ip, memo, siteSeq)
        }

    }
}

package kr.co.jsol.domain.entity.sensorDevice.dto.response

import com.querydsl.core.annotations.QueryProjection
import kr.co.jsol.domain.entity.sensorDevice.SensorDevice

data class SensorDeviceResponse @QueryProjection constructor(
    val sensorDeviceId: Long,
    val type: String,
    val modelName: String,
    val serialNumber: String,
    val ip: String,
    val memo: String,
    val siteName: String,
    val siteSeq: Long,
) {
    constructor(
        sensorDevice: SensorDevice
    ) : this(
        sensorDevice.id!!,
        sensorDevice.type,
        sensorDevice.modelName,
        sensorDevice.serialNumber,
        sensorDevice.ip,
        sensorDevice.memo,
        sensorDevice.site.name,
        sensorDevice.site.id!!
    )
}

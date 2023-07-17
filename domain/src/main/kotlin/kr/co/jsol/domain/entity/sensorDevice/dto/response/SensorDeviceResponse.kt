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
    val siteName: String?, // site가 삭제되면 null일 수 있음
    val siteSeq: Long?, // site가 삭제되면 null일 수 있음
    val imgPath: String,
) {
    constructor(
        sensorDevice: SensorDevice,
    ) : this(
        sensorDeviceId = sensorDevice.id!!,
        type = sensorDevice.type,
        modelName = sensorDevice.modelName,
        serialNumber = sensorDevice.serialNumber,
        ip = sensorDevice.ip,
        memo = sensorDevice.memo,
        siteName = sensorDevice.site?.name,
        siteSeq = sensorDevice.site?.seq,
        imgPath = sensorDevice.imgPath,
    )
}

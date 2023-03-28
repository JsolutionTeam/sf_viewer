package kr.co.jsol.domain.entity.sensorDevice

import kr.co.jsol.common.exception.MyEntityNotFoundException
import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceCreateRequest
import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceSearchCondition
import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceUpdateRequest
import kr.co.jsol.domain.entity.sensorDevice.dto.response.SensorDeviceResponse
import kr.co.jsol.domain.entity.site.SiteRepository
import org.springframework.stereotype.Service

@Service
class SensorDeviceService(
    private val sensorDeviceRepository: SensorDeviceRepository,
    private val siteRepository: SiteRepository,
) {
    fun saveSensorDevice(sensorDeviceCreateRequest: SensorDeviceCreateRequest): Long {
        val siteSeq = sensorDeviceCreateRequest.siteSeq!!

        // siteSeq로 site 정보 가져오기
        val optional = siteRepository.findById(siteSeq)
        if (optional.isEmpty) {
            throw IllegalArgumentException("존재하지 않는 농장 번호입니다.")
        }
        val site = optional.get()

        // site의 ip 정보 업데이트

        // dto to entity
        val sensorDevice = sensorDeviceCreateRequest.toEntity(site)
        sensorDevice.createdBy("SMART_FARM") // 관리자만 등록함

        return sensorDeviceRepository.save(sensorDevice).id!!
    }

    fun getSensorDevices(siteSearchCondition: SensorDeviceSearchCondition?): List<SensorDeviceResponse> {
        return sensorDeviceRepository.getSensorDeviceList(siteSearchCondition)
    }

    fun getSensorDeviceById(sensorDeviceId: Long): SensorDeviceResponse {
        val optional = sensorDeviceRepository.findById(sensorDeviceId)
        if (optional.isEmpty) {
            throw IllegalArgumentException("존재하지 않는 센서 기기 번호입니다.")
        }
        return SensorDeviceResponse.of(optional.get())
    }

    fun updateSensorDevice(sensorDeviceId: Long, sensorDeviceUpdateRequest: SensorDeviceUpdateRequest): Unit {
        // sensorDeviceId로 sensorDevice 정보 가져오기
        val optional = sensorDeviceRepository.findById(sensorDeviceId)
        if (optional.isEmpty) {
            throw IllegalArgumentException("존재하지 않는 센서 기기 번호입니다.")
        }
        val sensorDevice = optional.get()

        if(sensorDeviceUpdateRequest.siteSeq != null) {
            val siteSeq = sensorDeviceUpdateRequest.siteSeq
            // siteSeq로 site 정보 가져오기
            siteRepository.findById(siteSeq).let {
                if (it.isEmpty) {
                    throw IllegalArgumentException("존재하지 않는 농장 번호입니다.")
                }
                val site = it.get()
                sensorDevice.updateSite(site)
            }
        }

        sensorDevice.updateDeviceInfo(
            ip = sensorDeviceUpdateRequest.ip,
            memo = sensorDeviceUpdateRequest.memo,
        )
        sensorDevice.updatedBy("SMART_FARM") // 관리자만 수정함
        sensorDeviceRepository.save(sensorDevice)
    }

    fun deleteSensorDevice(sensorDeviceId: Long): Unit {
        val optional = sensorDeviceRepository.findById(sensorDeviceId)
        if (optional.isEmpty) {
            throw IllegalArgumentException("존재하지 않는 센서 기기 번호입니다.")
        }
        val sensorDevice: SensorDevice = optional.get()

        sensorDevice.softDelete("SMART_FARM") // 관리자만 삭제함

        sensorDeviceRepository.save(sensorDevice)
    }

}

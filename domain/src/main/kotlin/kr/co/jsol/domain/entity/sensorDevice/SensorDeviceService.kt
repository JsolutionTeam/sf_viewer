package kr.co.jsol.domain.entity.sensorDevice

import kr.co.jsol.domain.common.FileUploadService
import kr.co.jsol.domain.common.FileUtils
import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceCreateRequest
import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceSearchCondition
import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceUpdateRequest
import kr.co.jsol.domain.entity.sensorDevice.dto.response.SensorDeviceResponse
import kr.co.jsol.domain.entity.site.SiteRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
class SensorDeviceService(
    private val sensorDeviceRepository: SensorDeviceRepository,
    private val siteRepository: SiteRepository,
    private val fileUploadService: FileUploadService,
) {

    @Value("\${file.uploadDir}")
    private lateinit var uploadDir: String

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
        return SensorDeviceResponse(optional.get())
    }

    fun updateSensorDevice(sensorDeviceId: Long, sensorDeviceUpdateRequest: SensorDeviceUpdateRequest) {
        // sensorDeviceId로 sensorDevice 정보 가져오기
        val optional = sensorDeviceRepository.findById(sensorDeviceId)
        if (optional.isEmpty) {
            throw IllegalArgumentException("존재하지 않는 센서 기기 번호입니다.")
        }
        val sensorDevice = optional.get()

        // 농장 정보를 변경한다면,
        if (sensorDeviceUpdateRequest.siteSeq != null) {
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

        // 센서 장비 정보 수정
        sensorDevice.updateDeviceInfo(
            ip = sensorDeviceUpdateRequest.ip,
            memo = sensorDeviceUpdateRequest.memo,
        )

        // 수집 센서 정보 수정
        sensorDevice.updateSensorInfo(
            type = sensorDeviceUpdateRequest.type,
            unit = sensorDeviceUpdateRequest.unit,
            modelName = sensorDeviceUpdateRequest.modelName,
            serialNumber = sensorDeviceUpdateRequest.serialNumber,
        )
        sensorDevice.updatedBy("SMART_FARM") // 관리자만 수정함
        sensorDeviceRepository.save(sensorDevice)
    }

    fun updateSensorDeviceImage(sensorDeviceId: Long, imgFile: MultipartFile) {
        val optional = sensorDeviceRepository.findById(sensorDeviceId)
        if (optional.isEmpty) {
            throw IllegalArgumentException("존재하지 않는 센서 기기 번호입니다.")
        }
        val sensorDevice: SensorDevice = optional.get()

        val fileName = fileUploadService.uploadFile(imgFile, "sensorDevice/${sensorDeviceId}/")
        sensorDevice.updateImage(fileName)
        sensorDevice.updatedBy("SMART_FARM") // 관리자만 수정함
        sensorDeviceRepository.save(sensorDevice)
    }

    fun deleteSensorDevice(sensorDeviceId: Long) {
        val optional = sensorDeviceRepository.findById(sensorDeviceId)
        if (optional.isEmpty) {
            throw IllegalArgumentException("존재하지 않는 센서 기기 번호입니다.")
        }
        val sensorDevice: SensorDevice = optional.get()

        sensorDevice.softDelete("SMART_FARM") // 관리자만 삭제함

        sensorDeviceRepository.save(sensorDevice)
    }

}

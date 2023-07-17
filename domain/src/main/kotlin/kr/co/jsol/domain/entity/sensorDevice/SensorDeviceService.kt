package kr.co.jsol.domain.entity.sensorDevice

import kr.co.jsol.common.exception.entities.site.SiteNotFoundException
import kr.co.jsol.domain.common.FileService
import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceCreateRequest
import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceSearchCondition
import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceUpdateRequest
import kr.co.jsol.domain.entity.sensorDevice.dto.response.SensorDeviceResponse
import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.site.SiteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class SensorDeviceService(
    private val sensorDeviceRepository: SensorDeviceRepository,
    private val siteRepository: SiteRepository,
    private val fileService: FileService,
) {

    fun saveSensorDevice(sensorDeviceCreateRequest: SensorDeviceCreateRequest): Long {
        val site: Site? = sensorDeviceCreateRequest.siteSeq?.let {
            siteRepository.findBySeq(it)
        }

        val sensorDevice: SensorDevice = sensorDeviceCreateRequest.toEntity(site)
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

    @Transactional
    fun updateSensorDevice(sensorDeviceId: Long, sensorDeviceUpdateRequest: SensorDeviceUpdateRequest) {
        // sensorDeviceId로 sensorDevice 정보 가져오기
        val optional = sensorDeviceRepository.findById(sensorDeviceId)
        if (optional.isEmpty) {
            throw IllegalArgumentException("존재하지 않는 센서 기기 번호입니다.")
        }
        val sensorDevice = optional.get()

        // 장비의 농장 정보를 변경한다면,
        sensorDeviceUpdateRequest.siteSeq?.let{
            // 존재하지 않다면 null 로 넣는다
            val site: Site = siteRepository.findBySeq(it) ?: throw SiteNotFoundException()
            sensorDevice.updateSite(site)
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

        val fileName = fileService.uploadFile(imgFile, "sensorDevice/$sensorDeviceId/")
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

        // 파일도 같이 삭제돼야함.
        fileService.deleteFile(sensorDevice.imgPath)

        sensorDeviceRepository.delete(sensorDevice)
    }
}

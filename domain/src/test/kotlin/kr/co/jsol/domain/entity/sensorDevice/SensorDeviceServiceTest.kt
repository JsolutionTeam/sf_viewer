package kr.co.jsol.domain.entity.sensorDevice

import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceCreateRequest
import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceUpdateRequest
import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.site.SiteRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@DisplayName("센서 기기 테스트")
class SensorDeviceServiceTest @Autowired constructor(
    private val sensorDeviceRepository: SensorDeviceRepository,
    private val sensorDeviceService: SensorDeviceService,

    private val siteRepository: SiteRepository,
) {
    private val site = Site(name = "양파", crop = "양파", location = "테스트")

    @BeforeEach
    fun setUp() {
        sensorDeviceRepository.deleteAll()
        // 기본 농가 정보를 등록한다.
        siteRepository.save(site)
    }

    @Test
    @DisplayName("센서 기기 등록이 정상적으로 동작한다.")
    fun saveSensorDevice() {
        // given
        val sensorDeviceCreateRequest = SensorDeviceCreateRequest(
            deviceNo = "deviceNo",
            serialNumber = "serialNumber",
            ip = "ip",
            memo = "memo",
            siteSeq = site.id!!,
        )

        // when
        val sensorDeviceId = sensorDeviceService.saveSensorDevice(sensorDeviceCreateRequest)

        // then
        val sensorDevice = sensorDeviceRepository.findById(sensorDeviceId).get()
        assertThat(sensorDevice.deviceNo).isEqualTo("deviceNo")
        assertThat(sensorDevice.serialNumber).isEqualTo("serialNumber")
        assertThat(sensorDevice.ip).isEqualTo("ip")
        assertThat(sensorDevice.memo).isEqualTo("memo")
    }

    @Test
    @DisplayName("센서 기기 수정이 정상적으로 동작한다.")
    fun updateSensorDevice() {
        // given

        val saveSensorDevice = sensorDeviceRepository.save(
            SensorDevice(
                id = 1L,
                deviceNo = "deviceNo",
                serialNumber = "serialNumber",
                ip = "ip",
                memo = "memo",
                site = site
            )
        )

        val sensorDeviceUpdateRequest = SensorDeviceUpdateRequest(
            ip = "ip2",
            memo = "memo2",
            siteSeq = site.id!!,
        )

        // when
        sensorDeviceService.updateSensorDevice(saveSensorDevice.id!!, sensorDeviceUpdateRequest,)

        // then
        val sensorDevice = sensorDeviceRepository.findById(saveSensorDevice.id!!).get()

        assertThat(sensorDevice.deviceNo).isEqualTo("deviceNo")
        assertThat(sensorDevice.serialNumber).isEqualTo("serialNumber")
        assertThat(sensorDevice.ip).isEqualTo("ip2")
        assertThat(sensorDevice.memo).isEqualTo("memo2")
    }

    @Test
    @DisplayName("센서 기기 삭제가 정상적으로 동작한다.")
    fun deleteSensorDevice() {
        // given
        val saveSensorDevice = sensorDeviceRepository.save(
            SensorDevice(
                id = 1L,
                deviceNo = "deviceNo",
                serialNumber = "serialNumber",
                ip = "ip",
                memo = "memo",
                site = site
            )
        )

        // when
        sensorDeviceService.deleteSensorDevice(saveSensorDevice.id!!)

        // then
        val sensorDevice = sensorDeviceRepository.findById(saveSensorDevice.id!!)

        assertThat(sensorDevice.isEmpty).isTrue
    }
}